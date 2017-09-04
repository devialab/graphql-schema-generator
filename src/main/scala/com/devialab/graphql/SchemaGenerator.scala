package com.devialab.graphql

import java.beans.{Introspector, PropertyDescriptor}
import java.lang.reflect._
import java.util.Optional
import javax.validation.constraints.NotNull

import com.devialab.graphql.IDL.{Directive, FieldType}
import com.devialab.graphql.SchemaGenerator.DirectiveProvider
import grizzled.slf4j.Logging


/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
class SchemaGenerator(idlWriter: IDLWriter) extends Logging{

  def generateIDL(c: Class[_], directiveProviders: DirectiveProvider*): Unit  = {
    val info = Introspector.getBeanInfo(c)
    idlWriter.startType(info.getBeanDescriptor.getName)
    val processedProperties = info.getPropertyDescriptors.map(prop => {
      writeField(prop, directiveProviders:_*)
      prop.getName
    })
    c.getDeclaredFields
      .filter(f => !processedProperties.contains(f.getName) && (ScalaUtil.isCaseClass(c) || Modifier.isPublic(f.getModifiers)))
      .foreach(f => writeField(f, directiveProviders:_*))
    idlWriter.endType()
  }

  private def writeField(property: PropertyDescriptor, directiveProviders: DirectiveProvider*): Unit = {
    debug(s"Processing property ${property.getName}")
    fieldType(property.getPropertyType, property.getReadMethod.getGenericReturnType, isNotNull(property)) match {
      case Some(fieldType) => idlWriter.writeField(property.getName, fieldType, directiveProviders.flatMap(f => f(property.getName, fieldType)):_*)
      case None => warn(s"Ignoring property ${property.getName}")
    }
  }

  private def writeField(property: Field, directiveProviders: DirectiveProvider*): Unit = {
    debug(s"Processing property ${property.getName}")
    fieldType(property.getType, property.getGenericType, isNotNull(property)) match {
      case Some(fieldType) => idlWriter.writeField(property.getName, fieldType, directiveProviders.flatMap(f => f(property.getName, fieldType)):_*)
      case None => warn(s"Ignoring property ${property.getName}")
    }
  }

  private def fieldType(c: Class[_], genericType: Type, nonNull: Boolean): Option[IDL.FieldType] = c match {
    case string if classOf[String].isAssignableFrom(string) =>  Some(IDL.String(nonNull))
    case int if isInt(int) => Some(IDL.Int(nonNull))
    case float if isFloat(float) => Some(IDL.Float(nonNull))
    case boolean if isBoolean(boolean) => Some(IDL.Boolean(nonNull))
    case option if classOf[Option[_]].isAssignableFrom(option) || classOf[Optional[_]].isAssignableFrom(option) =>
      genericArgumentType(genericType).flatMap(fieldType(_, null, false))
    case iterable if classOf[Iterable[_]].isAssignableFrom(iterable) =>
      genericArgumentType(genericType).flatMap(fieldType(_, null, nonNull))
    case customType if isNotIgnored(customType) => beanName(customType).map(name => IDL.CustomType(name, nonNull))
    case _ =>
      warn(s"Skiping type $c")
      None
  }

  private def isNotIgnored(c: Class[_]): Boolean = c != classOf[Class[_]]

  private def beanName(c: Class[_]): Option[String] = {
    val info = Introspector.getBeanInfo(c)
    Option(info.getBeanDescriptor).map(_.getName)
  }

  private def isInt(int: Class[_]): Boolean =
    classOf[Long].isAssignableFrom(int) ||
      classOf[Int].isAssignableFrom(int) ||
      classOf[Short].isAssignableFrom(int) ||
      classOf[Byte].isAssignableFrom(int) ||
      classOf[java.lang.Long].isAssignableFrom(int) ||
      classOf[java.lang.Integer].isAssignableFrom(int) ||
      classOf[java.lang.Short].isAssignableFrom(int) ||
      classOf[java.lang.Byte].isAssignableFrom(int)

  private def isFloat(float: Class[_]): Boolean =
    classOf[Float].isAssignableFrom(float) ||
      classOf[Double].isAssignableFrom(float)

  private def isBoolean(boolean: Class[_]): Boolean =
    classOf[Boolean].isAssignableFrom(boolean) ||
      classOf[java.lang.Boolean].isAssignableFrom(boolean)

  private def genericArgumentType(c: Type): Option[Class[_]] = c match {
    case t: ParameterizedType =>
      val arg0 = t.getActualTypeArguments()(0)
      if(classOf[Class[_]].isAssignableFrom(arg0.getClass)) {
        Some(classOf[Class[_]].cast(arg0))
      }
      else if(classOf[ParameterizedType].isAssignableFrom(arg0.getClass)) {
        Some(classOf[Class[_]].cast(classOf[ParameterizedType].cast(arg0).getRawType))
      }
      else {
        None
      }
    case _ => None
  }

  private def isNotNull(property: PropertyDescriptor): Boolean =
    property.getPropertyType.isPrimitive || isNotNull(property.getReadMethod) || isNotNull(property.getWriteMethod) || LombokUtils.isNonNull(property)

  private def isNotNull(obj: AccessibleObject): Boolean =
    isPrimitiveField(obj) || Option(obj).exists(_.isAnnotationPresent(classOf[NotNull]))

  private def isPrimitiveField(obj: AccessibleObject): Boolean = {
    Option(obj).exists(x => x.isInstanceOf[Field] && classOf[Field].cast(x).getType.isPrimitive)
  }
}
object SchemaGenerator {

  type DirectiveProvider = ((String, FieldType) => Option[Directive])

  def apply(idlWriter: IDLWriter): SchemaGenerator = new SchemaGenerator(idlWriter)
}
