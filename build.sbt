organization := "com.devialab"

name := "graphql-schema-generator"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.3"


libraryDependencies ++= Seq(
  "org.projectlombok" % "lombok" % "1.16.18" % "provided",
  "javax.validation" % "validation-api" % "2.0.0.Final" % "provided",
  "org.clapper" %% "grizzled-slf4j" % "1.3.1",
  "com.kdgregory.bcelx" % "bcelx" % "1.0.0",
  "org.scala-lang" % "scala-reflect" % "2.12.3",
  "io.github.lukehutch" % "fast-classpath-scanner" % "1.99.0",

  // Test dependencies ----
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "test"
)

publishMavenStyle := true

compileOrder := CompileOrder.JavaThenScala