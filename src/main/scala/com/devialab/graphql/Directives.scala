package com.devialab.graphql

import com.devialab.graphql.IDL.CustomType

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object Directives {

  val Neo4JOutRelationship: SchemaGenerator.DirectiveProvider = {
    case (field: String, _: CustomType | IDL.List(_: CustomType, _)) =>
      Some(IDL.Directive("relation", Map("name" -> field, "direction" -> "OUT")))
    case _ => None
  }

}
