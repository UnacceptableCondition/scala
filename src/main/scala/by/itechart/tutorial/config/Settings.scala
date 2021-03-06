package by.itechart.tutorial.config

import com.typesafe.config.{Config, ConfigFactory}

object Settings {
  val config: Config = ConfigFactory.load()
  val flywayConfig: Config = config.getConfig("flyway")
  val dbPropertiesConfig: Config = config.getConfig("database").getConfig("properties")

  val defaultUserIsActive: Boolean = config.getBoolean("defaultUserIsActive")
  val serverHost: String = config.getString("serverHost")
  val serverPort: Int = config.getInt("serverPort")

  val dbUrl: String = dbPropertiesConfig.getString("url")
  val dbUser: String = dbPropertiesConfig.getString("user")
  val dbPassword: String = dbPropertiesConfig.getString("password")
  val baselineOnMigrate: Boolean = flywayConfig.getBoolean("baselineOnMigrate")
}
