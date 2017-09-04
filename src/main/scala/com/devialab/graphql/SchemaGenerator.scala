package com.devialab.graphql

import java.beans.{Introspector, PropertyDescriptor}
import java.lang.reflect._
import java.util.Optional
import javax.validation.constraints.NotNull

import com.devialab.graphql.IDL.{Directive, FieldType}
import com.devialab.graphql.SchemaGenerator.DirectiveProvider
import com.devialab.graphql.annotation.WrapperGenericType
import grizzled.slf4j.Logging
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner


/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
class SchemaGenerator(idlWriter: IDLWriter) extends Logging {

  def generateIDL(packageName: String, filter: (Class[_] => Boolean) , directiveProviders: DirectiveProvider*): Unit = {
    val scanner = new FastClasspathScanner(packageName)
    var classes = Seq.empty[Class[_]]
    scanner.matchAllStandardClasses(classes :+= _)
    scanner.scan()
    generateIDL(classes.filter(filter), directiveProviders:_*)
  }

  def generateIDL(c: Class[_], directiveProviders: DirectiveProvider*): Unit = {
    generateIDL(Seq(c), directiveProviders:_*)
  }

  def generateIDL(classes: Seq[Class[_]], directiveProviders: DirectiveProvider*): Unit = {
    var classesToProcess = classes.toSet
    var processedClasses = Set.empty[Class[_]]
    while(classesToProcess.nonEmpty) {
      val c = classesToProcess.head
      classesToProcess = classesToProcess.tail ++ (doGenerateIDL(c, directiveProviders:_*) diff processedClasses)
      processedClasses += c
    }
  }

  private def doGenerateIDL(c: Class[_], directiveProviders: DirectiveProvider*): Set[Class[_]]  = {
    debug(s"Generating IDL for $c")
    var customClasses = Set.empty[Class[_]]
    c match {
      case javaEnum if classOf[Enum[_]].isAssignableFrom(javaEnum) =>
        idlWriter.startEnum(javaEnum.getSimpleName)
        c.getEnumConstants.foreach(const => idlWriter.writeEnumValue(classOf[Enum[_]].cast(const).name()))
        idlWriter.endEnum()
      case _ =>
        val info = Introspector.getBeanInfo(c)
        idlWriter.startType(info.getBeanDescriptor.getName)
        val processedProperties = info.getPropertyDescriptors.map(prop => {
          writeField(prop, directiveProviders:_*) match {
            case Some(IDL.CustomType(_, _, Some(c))) => customClasses += c
            case _ => //nothing to do if not custom
          }
          prop.getName
        })
        c.getDeclaredFields
          .filter(f => !processedProperties.contains(f.getName) && (ScalaUtil.isCaseClass(c) || Modifier.isPublic(f.getModifiers)))
          .foreach(f => writeField(f, directiveProviders:_*) match {
            case Some(IDL.CustomType(_, _, Some(c))) => customClasses += c
            case _ => //nothing to do if not custom
          })
        idlWriter.endType()
    }
    customClasses
  }

  private def writeField(property: PropertyDescriptor, directiveProviders: DirectiveProvider*): Option[FieldType] = {
    debug(s"Processing property ${property.getName}")
    fieldType(property.getPropertyType, property.getReadMethod.getGenericReturnType, isNotNull(property)) match {
      case Some(fieldType) =>
        idlWriter.writeField(property.getName, fieldType, directiveProviders.flatMap(f => f(property.getName, fieldType)):_*)
        Some(fieldType)
      case None =>
        warn(s"Ignoring property ${property.getName}")
        None
    }
  }

  private def writeField(property: Field, directiveProviders: DirectiveProvider*): Option[FieldType] = {
    debug(s"Processing property ${property.getName}")
    fieldType(property.getType, property.getGenericType, isNotNull(property)) match {
      case Some(fieldType) =>
        idlWriter.writeField(property.getName, fieldType, directiveProviders.flatMap(f => f(property.getName, fieldType)):_*)
        Some(fieldType)
      case None =>
        warn(s"Ignoring property ${property.getName}")
        None
    }
  }

  private def fieldType(c: Class[_], genericType: Type, nonNull: Boolean): Option[IDL.FieldType] = {
    debug(s"Computing field type for class $c and genericType $genericType")
    c match {
      case knownType if KnownTypes.get(knownType).isDefined => KnownTypes.get(knownType)
      case string if classOf[String].isAssignableFrom(string) =>  Some(IDL.String(nonNull))
      case int if isInt(int) => Some(IDL.Int(nonNull))
      case float if isFloat(float) => Some(IDL.Float(nonNull))
      case boolean if isBoolean(boolean) => Some(IDL.Boolean(nonNull))
      case option if classOf[Option[_]].isAssignableFrom(option) || classOf[Optional[_]].isAssignableFrom(option) =>
        genericArgumentType(genericType)
          .flatMap(generic => castTypeToClass(generic).map((_, generic)))
          .flatMap({case (argClass, generic) => fieldType(argClass, generic, false) })
      case array if array.isArray => fieldType(array.getComponentType, null, isNotNull(array.getComponentType)).map(IDL.List(_, nonNull))
      case iterable if isList(iterable) =>
        genericArgumentType(genericType)
          .flatMap(generic => castTypeToClass(generic).map((_, generic)))
          .flatMap({case (argClass, generic) =>  fieldType(argClass, generic, isNotNull(argClass)) })
          .map(IDL.List(_, nonNull))
      case wrapper if wrapper.isAnnotationPresent(classOf[WrapperGenericType]) =>
        genericArgumentType(genericType, wrapper.getAnnotation(classOf[WrapperGenericType]).typeArgument())
          .flatMap(generic => castTypeToClass(generic).map((_, generic)))
          .flatMap({case (argClass, generic) => fieldType(argClass, generic, isNotNull(argClass)) })
      case javaEnum if classOf[Enum[_]].isAssignableFrom(javaEnum) => Some(IDL.CustomType(javaEnum.getSimpleName, false, Some(javaEnum)))
      case customType if isNotIgnored(customType) => beanName(customType).map(name => IDL.CustomType(name, nonNull, Some(customType)))
      case _ =>
        warn(s"Skiping type $c")
        None
    }
  }

  private def isNotIgnored(c: Class[_]): Boolean = c != classOf[Class[_]]

  private def beanName(c: Class[_]): Option[String] = {
    val info = Introspector.getBeanInfo(c)
    Option(info.getBeanDescriptor).map(_.getName)
  }

  private def isList(iterable: Class[_]): Boolean =
    classOf[Iterable[_]].isAssignableFrom(iterable) ||
    classOf[java.lang.Iterable[_]].isAssignableFrom(iterable)

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

  private def genericArgumentType(c: Type, typeArgumentIndex: Int = 0): Option[Type] = c match {
    case t: ParameterizedType =>
      val args = t.getActualTypeArguments()
      if(args.size > typeArgumentIndex) {
        Some(args(typeArgumentIndex))
      }
      else {
        None
      }
    case _ => None
  }

  private def castTypeToClass(t: Type): Option[Class[_]] = {
    if(classOf[Class[_]].isAssignableFrom(t.getClass)) {
      Some(classOf[Class[_]].cast(t))
    }
    else if(classOf[ParameterizedType].isAssignableFrom(t.getClass)) {
      Some(classOf[Class[_]].cast(classOf[ParameterizedType].cast(t).getRawType))
    }
    else {
      None
    }
  }

  private def isNotNull(c: Class[_]): Boolean = c.isPrimitive

  private def isNotNull(property: PropertyDescriptor): Boolean =
    isNotNull(property.getPropertyType) || isNotNull(property.getReadMethod) || isNotNull(property.getWriteMethod) || LombokUtils.isNonNull(property)

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
