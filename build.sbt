name := "ScalaWebService"

version := "0.1"

libraryDependencies ++= Seq(
  "net.codingwell" %% "scala-guice" % "4.2.6",

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
//
//resolvers += Resolver.url("org.flywaydb",
//  new URL("https://repo1.maven.org/maven2/org/flywaydb/flyway-sbt/4.2.0/flyway-sbt-4.2.0.pom"))(
//  Resolver.defaultIvyPatterns)
//
//resolvers += "org.flywaydb" at "https://repo1.maven.org/maven2/org/flywaydb/flyway-sbt/4.2.0/flyway-sbt-4.2.0.pom"


//resolvers += "Flyway" at "https://flywaydb.org/repo"
scalaVersion := "2.12.1"
