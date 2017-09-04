package com.devialab.graphql

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object IDL {

  sealed trait FieldType {
    val nonNull: scala.Boolean
  }

  sealed trait ScalarType extends FieldType {
    override def toString: Predef.String = getClass.getSimpleName + (if(nonNull) "!" else "")
  }
  case class Int(nonNull: scala.Boolean = false) extends ScalarType
  case class Float(nonNull: scala.Boolean = false) extends ScalarType
  case class String(nonNull: scala.Boolean = false) extends ScalarType
  case class Boolean(nonNull: scala.Boolean = false) extends ScalarType
  case class ID(nonNull: scala.Boolean = false) extends ScalarType

  case class List(scalarType: FieldType, nonNull: scala.Boolean = false) extends FieldType {
    override def toString: Predef.String = s"[$scalarType]" + (if(nonNull) "!" else "")
  }

  case class CustomType(name: Predef.String, nonNull: scala.Boolean = false, clazz: Option[Class[_]] = None) extends FieldType {
    override def toString: Predef.String = name + (if(nonNull) "!" else "")
  }

  case class Directive(name: Predef.String, properties: Map[Predef.String, Predef.String] = Map.empty) {
    override def toString: Predef.String = s"@$name(${properties.map({ case (k,v) => s""""$k":"$v""""}).mkString(" ")})"
  }

}
