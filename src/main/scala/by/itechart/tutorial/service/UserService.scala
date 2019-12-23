package by.itechart.tutorial.service

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.dao.{RepositoriesManager, User, UserActivityEntity, UserToGroupEntity}
import com.google.inject.Inject
import com.google.inject.name.Named
import by.itechart.tutorial.dao.JdbcProfilesManager.profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserService @Inject()(@Named("db") db: Database, val repositoriesManager: RepositoriesManager) {

  private val userRep = repositoriesManager.userRepository
  private val userToGroupRep = repositoriesManager.userToGroupRepository
  private val userActivityRep = repositoriesManager.userActivityRepository

  def getUserById(id: Long): Future[Option[User]] = userRep.findById(id)

  def saveUser(user: User): Future[Option[User]] = {
    val basicQuery = (for {
      Some(u) <- userRep.insertAction(user)
      _ <- userActivityRep.insertAction(UserActivityEntity(Option.empty, u.id.get, u.isActive, u.lastUpdateTime))
    } yield Some(u)).transactionally

    val action =
      if (Settings.defaultUserIsActive)
        (for {
          Some(u) <- basicQuery
          _ <- userToGroupRep.insertAction(UserToGroupEntity(Option.empty, u.id.get, Settings.defaultGroupId))
        } yield Some(u)).transactionally
      else
        basicQuery

    run(action)
  }

  def deleteUserById(id: Long): Future[Option[User]] = {
    val query = (for {
      user <- userRep.filterByIdAction(id).result
      _ <- userActivityRep.filterByUserIdAction(id).delete
      _ <- userToGroupRep.filterByUserIdAction(id).delete
      _ <- userRep.deleteAction(id)
    } yield user.headOption).transactionally

    run(query)
  }

  def changeUserActivityStatus(id: Long, status: Boolean): Future[Unit] = {
    val basicQuery = for {
      Some(activity) <- userActivityRep.filterByUserIdAction(id).result.headOption
      _ <- userRep.patchUserActivityAction(id, activity, status)
    } yield activity

    val query = if (status)
      (for {
        _ <- basicQuery
        _ <- userToGroupRep.pathAction(id, Settings.defaultGroupId)
      } yield ()).transactionally
    else
      (for {
        _ <- basicQuery
        _ <- userToGroupRep.filterByUserIdAction(id).delete
      } yield ()).transactionally

    run(query)
  }

  def getFullUserInfoById(id: Long): Future[Option[(User, Seq[String])]] = {
    run(userRep.findByIdFullInfoAction(id).result).map({
      case t if t.nonEmpty => Some(t.foldLeft(t.head._1, Seq[String]())((pref, next) => {
        (pref._1, pref._2 :+ next._2.map(_.name).getOrElse(""))
      }))
      case _ => None
    })
  }

  def updateUserById(id: Long)(user: User): Future[Option[User]] = userRep.update(id, user)

  def getAllUsers: Future[Seq[User]] = userRep.findAll()

  def getUsersWitOffsetAndLimit(offset: Int)(limit: Int)(): Future[Seq[User]] = userRep.findWithOffsetAndLimit(offset, limit)

  def getUsersFirstPage: Future[Seq[User]] = userRep.findFirstPage()

  def run[M](action: DBIO[M]): Future[M] = {
    db.run(action)
  }
}
