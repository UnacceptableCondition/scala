package by.itechart.tutorial.config

import com.typesafe.config.{Config, ConfigFactory}

import scala.sys.SystemProperties


object Settings {
  val systemProperties = new SystemProperties

  val config: Config = ConfigFactory.load()
  val flywayConfig: Config = config.getConfig("flyway")

  val db = systemProperties.get("db")

  val dbPropertiesConfig: Config = config
    .getConfig(db.getOrElse("database"))
    .getConfig("properties")

  val swaggerPropertiesConfig: Config = config.getConfig("swagger")
  val businessPropertiesConfig: Config = config.getConfig("business")

  val defaultUserIsActive: Boolean = config.getBoolean("defaultUserIsActive")
  val serverHost: String = config.getString("serverHost")
  val serverPort: Int = config.getInt("serverPort")

  val dbUrl: String = dbPropertiesConfig.getString("url")
  val dbUser: String = dbPropertiesConfig.getString("user")
  val dbPassword: String = dbPropertiesConfig.getString("password")
  val dbProfile: String = config.getConfig(db.getOrElse("database")).getString("profile")

  val baselineOnMigrate: Boolean = flywayConfig.getBoolean("baselineOnMigrate")

  val swaggerApiDocsPath: String = swaggerPropertiesConfig.getString("apiDocsPath")
  val swaggerHost: String = swaggerPropertiesConfig.getString("host")
  val swaggerPOrt: Int = swaggerPropertiesConfig.getInt("port")

  val defaultGroupId: Int = businessPropertiesConfig.getInt("defaultGroupId")
}
