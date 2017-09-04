package com.devialab.graphql

import java.beans.PropertyDescriptor
import java.lang.reflect.Field

import com.kdgregory.bcelx.parser.AnnotationParser
import lombok.NonNull
import org.apache.bcel.Repository

import scala.collection.JavaConverters._

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object LombokUtils {

  def isNonNull(property: PropertyDescriptor): Boolean = {
    val javaClass = Repository.lookupClass(property.getReadMethod.getDeclaringClass)
    val parser = new AnnotationParser(javaClass)
    javaClass.getMethods
      .find(_.getName == property.getReadMethod.getName)
      .exists(parser.getMethodAnnotations(_).asScala.exists(_.getClassName == classOf[NonNull].getName))
  }

  def isNonNull(field: Field): Boolean = {
    val javaClass = Repository.lookupClass(field.getDeclaringClass)
    val parser = new AnnotationParser(javaClass)
      javaClass.getFields
      .find(_.getName == field.getName)
      .exists(parser.getFieldAnnotations(_).containsKey(classOf[NonNull].getName))
  }





}
