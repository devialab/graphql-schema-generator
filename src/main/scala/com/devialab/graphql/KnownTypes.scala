package com.devialab.graphql

import java.time.LocalDateTime
import java.util.Date

import com.devialab.graphql.IDL.FieldType

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object KnownTypes {

  def get(c: Class[_]): Option[FieldType] = c match {
    case date if isDate(c) => Some(IDL.String())
    case _ => None
  }

  private def isDate(c: Class[_]): Boolean =
    classOf[LocalDateTime].isAssignableFrom(c) ||
    classOf[Date].isAssignableFrom(c)

}
