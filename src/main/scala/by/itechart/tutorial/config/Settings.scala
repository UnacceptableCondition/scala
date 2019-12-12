package by.itechart.tutorial.config

import com.typesafe.config.{Config, ConfigFactory}

object Settings {
  val config: Config = ConfigFactory.load()

  val defaultUserIsActive: Boolean = config.getBoolean("defaultUserIsActive")
  val serverHost: String = config.getString("serverHost")
  val serverPort: Int = config.getInt("serverPort")
}
