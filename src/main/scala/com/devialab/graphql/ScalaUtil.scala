package com.devialab.graphql


/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object ScalaUtil {

  def isCaseClass(c: Class[_]): Boolean = {
    import scala.reflect.runtime.universe._
    val typeMirror = runtimeMirror(c.getClassLoader)
    typeMirror.reflectClass(typeMirror.classSymbol(c)).symbol.isCaseClass
  }

}
