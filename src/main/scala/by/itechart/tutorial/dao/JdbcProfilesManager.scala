package by.itechart.tutorial.dao

import by.itechart.tutorial.config.Settings
import slick.jdbc.{H2Profile, JdbcProfile, PostgresProfile}

object JdbcProfilesManager {
    val profile: JdbcProfile = Settings.dbProfile match {
        case "H2" => H2Profile
        case "Postgres" => PostgresProfile
        case _ => PostgresProfile
    }
}

