package config

import com.google.inject.{AbstractModule, Provides}
import com.google.inject.name.Named
import net.codingwell.scalaguice.ScalaModule
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api.Database


class ApplicationConfig extends AbstractModule with ScalaModule {

  @Provides
  @Named("db")
  def provideDataSourceParams(): PostgresProfile.backend.DatabaseDef = {
    Database.forConfig("database")
  }

}
