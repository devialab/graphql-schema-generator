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
  "org.apache.commons" % "commons-lang3" % "3.6",


  // Test dependencies ----
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "test"
)

// Publishing settings
publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".sonatype-credentials")

compileOrder := CompileOrder.JavaThenScala

// Metadata

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/devialab/graphql-schema-generator"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/devialab/graphql-schema-generator"),
    "scm:git@github.com:devialab/graphql-schema-generator.git"
  )
)

developers := List(
  Developer(
    id    = "alexdeleon",
    name  = "Alexander De Leon",
    email = "alex@devialab.com",
    url   = url("https://github.com/alexdeleon")
  )
)