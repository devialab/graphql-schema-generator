package com.devialab.graphql

import com.devialab.graphql.IDL.CustomType

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
object Directives {

  val Neo4JOutRelationship: SchemaGenerator.DirectiveProvider = {
    case (field: String, fieldType: CustomType) => Some(IDL.Directive("out", Map("name" -> field)))
    case _ => None
  }

}
