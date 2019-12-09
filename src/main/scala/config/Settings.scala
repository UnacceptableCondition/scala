package config

import com.typesafe.config.Config
import javax.inject.Inject

class Settings @Inject() (config: Config) {

  val defaultUseIsActive: Boolean = config.getBoolean("defaultUseIsActive")
}
