package by.itechart.tutorial.config

import com.google.inject.{AbstractModule, Provides}
import com.google.inject.name.Named
import com.typesafe.config.{Config, ConfigFactory}
import net.codingwell.scalaguice.ScalaModule
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api.Database


class ApplicationConfig extends AbstractModule with ScalaModule {

  @Provides
  @Named("db")
  def provideDataSourceParams(): PostgresProfile.backend.DatabaseDef = {
    Database.forConfig("database")
  }

  @Provides
  def provideConfig(): Config = {
    ConfigFactory.load()
  }

}
