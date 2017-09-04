package com.devialab.graphql

import java.io.{BufferedWriter, Writer}
import IDLWriter._
/**
  * @author Alexander De Leon (alex.deleon@devialab.com)
  */
class IDLWriter(writer: BufferedWriter) {

  private var state: State.Value = State.Ready

  @throws[IllegalStateException]
  def startType(name: String): IDLWriter = {
    if(!isReady) throw new IllegalStateException(s"Cannot start a type while in $state state. Expected state to be Ready")
    writer.write(s"type $name {\n")
    state = State.TypeStarted
    this
  }

  @throws[IllegalStateException]
  def writeField(field: String, `type`: IDL.FieldType, directives: IDL.Directive*): IDLWriter = {
    if(state != State.TypeStarted) throw new IllegalStateException(s"Cannot write field while in $state state. Expected state to be TypeStarted")
    writer.write(s"\t$field: ${`type`} ${directives.map(_.toString).mkString(" ")}\n")
    this
  }

  @throws[IllegalStateException]
  def endType(): IDLWriter = {
    if(state != State.TypeStarted) throw new IllegalStateException(s"Cannot end a type while in $state state. Expected state to be TypeStarted")
    writer.write("}\n")
    state = State.Ready
    this
  }

  def isReady: Boolean = state == State.Ready

}

object IDLWriter {
  object State extends Enumeration {
    val Ready, TypeStarted = Value
  }
}
