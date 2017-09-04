package com.devialab.graphql

import java.time.LocalDateTime

import com.devialab.graphql.IDL.FieldType

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object KnownTypes {

  def get(c: Class[_]): Option[FieldType] = c match {
    case localDateTime if classOf[LocalDateTime].isAssignableFrom(localDateTime) => Some(IDL.String())
    case _ => None
  }

}
