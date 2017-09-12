package com.devialab.graphql

import java.lang.annotation.Annotation
import java.lang.reflect.Method

import org.apache.commons.lang3.ClassUtils.Interfaces
import org.apache.commons.lang3.reflect.MethodUtils

import scala.collection.JavaConverters._

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object AnnotationUtils {

  def findAnnotation[A <: Annotation](method: Method, c: Class[A]): Option[A] =
    MethodUtils.getOverrideHierarchy(method, Interfaces.INCLUDE).asScala
      .find(_.isAnnotationPresent(c))
      .map(_.getAnnotation(c))

}
