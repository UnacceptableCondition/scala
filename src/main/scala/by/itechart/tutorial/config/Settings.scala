package by.itechart.tutorial.config

import com.typesafe.config.Config
import javax.inject.Inject

class Settings @Inject()(config: Config) {

  val defaultUserIsActive: Boolean = config.getBoolean("defaultUserIsActive")
  val serverHost: String = config.getString("serverHost")
  val serverPort: Int = config.getInt("serverPort")
}
