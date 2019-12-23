package by.itechart.tutorial.config

import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import slick.jdbc.{JdbcBackend, JdbcProfile}
import slick.jdbc.JdbcBackend.Database
import by.itechart.tutorial.dao.JdbcProfilesManager

class ApplicationConfig extends AbstractModule with ScalaModule with MigrationConfig {

  val database = Database.forConfig("database")

  @Provides
  @Named("db")
  def provideDataSourceParams(): JdbcBackend.Database = database

  @Provides
  @Named("profile")
  def provideJdbcProfile(): JdbcProfile = JdbcProfilesManager.profile

}
