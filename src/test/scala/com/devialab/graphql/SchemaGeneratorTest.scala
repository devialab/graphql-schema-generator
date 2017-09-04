package com.devialab.graphql

import com.devialab.graphql.IDL.CustomType
import com.devialab.graphql.test.{TestJavaBean, TestLombokJavaBean}
import com.devialab.graphql.test.TestCaseClass
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}

import scala.collection.mutable.ArrayBuffer

/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
class SchemaGeneratorTest extends FunSuite with Matchers with MockFactory {

  test("scala case class") {
    val mockWriter = mock[IDLWriter]
    val generator = SchemaGenerator(mockWriter)

    (mockWriter.startType _).expects("TestCaseClass").once()
    (mockWriter.writeField _).expects("string", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("int", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("long", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("short", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("byte", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("boolean", IDL.Boolean(true), *).once()
    (mockWriter.writeField _).expects("float", IDL.Float(true), *).once()
    (mockWriter.writeField _).expects("double", IDL.Float(true), *).once()
    (mockWriter.writeField _).expects("optionString", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("customType", IDL.CustomType("TestCustomTypeCaseClass", false), *).once()
    (mockWriter.endType _).expects().once()


    generator.generateIDL(classOf[TestCaseClass])

  }

  test("java POJO bean") {
    val mockWriter = mock[IDLWriter]
    val generator = SchemaGenerator(mockWriter)

    (mockWriter.startType _).expects("TestJavaBean").once()

    (mockWriter.writeField _).expects("string", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("intValue", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("integerValue", IDL.Int(false), *).once()
    (mockWriter.writeField _).expects("longValue", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("shortValue", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("byteValue", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("notNullableString", IDL.String(true), *).once()
    (mockWriter.writeField _).expects("inmutableString", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("floatValue", IDL.Float(true), *).once()
    (mockWriter.writeField _).expects("doubleValue", IDL.Float(true), *).once()
    (mockWriter.writeField _).expects("booleanValue", IDL.Boolean(true), *).once()

    (mockWriter.writeField _).expects("privateField", *, *).never()
    (mockWriter.endType _).expects().once()

    generator.generateIDL(classOf[TestJavaBean])
  }

  test("java Lombok bean") {
    val mockWriter = mock[IDLWriter]
    val generator = SchemaGenerator(mockWriter)

    (mockWriter.startType _).expects("TestLombokJavaBean").once()

    (mockWriter.writeField _).expects("string", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("notNullableString", IDL.String(true), *).once()
    (mockWriter.endType _).expects().once()

    generator.generateIDL(classOf[TestLombokJavaBean])
  }

  test("directive provider") {
    val provider: SchemaGenerator.DirectiveProvider = Directives.Neo4JOutRelationship

    val mockWriter = mock[IDLWriter]
    val generator = SchemaGenerator(mockWriter)

    (mockWriter.startType _).expects("TestCaseClass").once()
    (mockWriter.writeField _).expects("string", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("int", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("long", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("short", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("byte", IDL.Int(true), *).once()
    (mockWriter.writeField _).expects("boolean", IDL.Boolean(true), *).once()
    (mockWriter.writeField _).expects("float", IDL.Float(true), *).once()
    (mockWriter.writeField _).expects("double", IDL.Float(true), *).once()
    (mockWriter.writeField _).expects("optionString", IDL.String(false), *).once()
    (mockWriter.writeField _).expects("customType", IDL.CustomType("TestCustomTypeCaseClass", false), Seq(IDL.Directive("out", Map("name" -> "customType")))).once()
    (mockWriter.endType _).expects().once()


    generator.generateIDL(classOf[TestCaseClass], provider)

  }

}