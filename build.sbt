name := "ScalaTutorial"

version := "0.1"

scalaVersion := "2.12.1"
libraryDependencies ++= Seq(
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.json4s" %% "json4s-native" % "3.7.0-M1",


  "com.typesafe.slick" % "slick_2.12" % "3.3.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.slick" % "slick-hikaricp_2.12" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",

  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
  "com.typesafe.akka" %% "akka-stream" % "2.6.0",
  "com.typesafe.akka" %% "akka-actor" % "2.6.0",
  "com.typesafe.akka" %% "akka-http"   % "10.1.11",

  "com.typesafe" % "config" % "1.4.0",
)
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.0.7")