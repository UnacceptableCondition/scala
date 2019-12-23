name := "ScalaTutorial"

version := "0.1"

scalaVersion := "2.12.1"
libraryDependencies ++= Seq(
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.json4s" %% "json4s-native" % "3.7.0-M1",


  "javax.ws.rs" % "javax.ws.rs-api" % "2.1",
  "javax.ws.rs" % "jsr311-api" % "1.1.1",
  "io.swagger" % "swagger-annotations" % "1.6.0",


  "com.typesafe.slick" % "slick_2.12" % "3.3.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.slick" % "slick-hikaricp_2.12" % "3.3.2",

  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
  "com.typesafe.akka" %% "akka-stream" % "2.6.0",
  "com.typesafe.akka" %% "akka-actor" % "2.6.0",
  "com.typesafe.akka" %% "akka-http"   % "10.1.11",
  "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.0.4",

  "com.typesafe" % "config" % "1.4.0",
  "org.flywaydb" % "flyway-core" % "6.1.1",

  "org.scalatest" %% "scalatest" % "3.1.0" % "test",
  "org.scalamock" %% "scalamock" % "4.4.0" % "test",
  "com.h2database" % "h2" % "1.4.192" % "test",
  "org.mockito" % "mockito-scala_2.12" % "1.10.1"
)