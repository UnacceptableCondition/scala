package by.itechart.tutorial.config

trait MigrationConfig {

  import org.flywaydb.core.Flyway

  val flyway: Flyway = Flyway
    .configure
    .dataSource(Settings.dbUrl, Settings.dbUser, Settings.dbPassword)
    .baselineOnMigrate(Settings.baselineOnMigrate)
    .load

  flyway.migrate()
}
