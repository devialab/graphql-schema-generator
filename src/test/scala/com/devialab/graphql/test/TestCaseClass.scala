package com.devialab.graphql.test

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
case class TestCaseClass(
                          string: String,
                          int: Int,
                          long: Long,
                          short: Short,
                          byte: Byte,
                          float: Float,
                          double: Double,
                          boolean: Boolean,
                          optionString: Option[String],
                          customType: TestCustomTypeCaseClass
                        )
