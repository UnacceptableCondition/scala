package services.dao

import com.google.inject.Inject
import com.google.inject.name.Named
import model.User
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UsersDAO @Inject() (@Named("db") db: Database) extends TableQuery(new Users(_)) {
  val defaultUsersQuantityToShow = 20

  def findByIdFull(id: Long): Future[Option[(User, Seq[String])]] = {
    val users = for {
      ((u, _), g)  <- Tables.users joinLeft Tables.userGroup on (_.id === _.userId) joinLeft Tables.groups on(_._2.map(_.groupId) === _.id)
      if u.id === id
    } yield (u, g)

    db.run(users.result).map({
      case t if t.nonEmpty => Some(t.foldLeft(t.head._1, Seq[String]())((pref, next) => {
        (pref._1, pref._2 :+ next._2.map(_.name).getOrElse(""))
      }))
      case _ => None
    })
  }

  def findById(id: Long): Future[Option[User]] = {
    db.run(this.filter(_.id === id).result).map(_.headOption)
  }

  def create(user: User): Future[User] = {
    db.run(this returning this.map(_.id) into ((usr, id) => usr.copy(id = Some(id))) += user)
  }

  def deleteById(id: Long): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }

  def getUsers: Future[Seq[User]] = getCertainQuantityUsers(defaultUsersQuantityToShow)
  def getAllUsers: Future[Seq[User]] = db.run(TableQuery[Users].result)

  def getCertainQuantityUsers(quantity: Int): Future[Seq[User]] = {
    db.run(this.take(if (quantity > 100 || quantity < 0) 100 else quantity).result)
  }

  def getUsersWithLimitAndOffset(offset: Int, limit: Int): Future[Seq[User]] = {
    db
      .run(this.drop(offset)
      .take(if (limit > 100 || limit < 0) 100 else limit).result)
  }
}
