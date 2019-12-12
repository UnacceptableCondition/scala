package by.itechart.tutorial.dao

import java.sql.{Date, Timestamp}

import by.itechart.tutorial.config.Settings
import com.google.inject.Inject
import com.google.inject.name.Named
import slick.jdbc.{JdbcProfile, PostgresProfile}
import by.itechart.tutorial.util.UtilFunctions.currentTime
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserRepository @Inject()(@Named("db") db: Database) extends Repository[Users, Long](db) {

  val profile: JdbcProfile = PostgresProfile
  val table = TableQuery[Users]

  def getId(t: Users): profile.api.Rep[Id] = t.id

  def copyWithId(model: User, id: Id): User = model.copy(id = Some(id))

  def findByIdFull(id: Id): Future[Option[(User, Seq[String])]] = {
    val users = for {
      ((u, _), g) <- Tables.users joinLeft Tables.userGroup on (_.id === _.userId) joinLeft Tables.groups on (_._2.map(_.groupId) === _.id)
      if u.id === id
    } yield (u, g)

    db.run(users.result).map({
      case t if t.nonEmpty => Some(t.foldLeft(t.head._1, Seq[String]())((pref, next) => {
        (pref._1, pref._2 :+ next._2.map(_.name).getOrElse(""))
      }))
      case _ => None
    })
  }
}


case class User(
                 id: Option[Long] = Option.empty,
                 name: String,
                 surname: String,
                 email: String,
                 dateOfBirth: Date,
                 creationDate: Timestamp = currentTime,
                 lastUpdateTime: Timestamp = currentTime,
                 isActive: Boolean = Settings.defaultUserIsActive
               )
