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
    val user = for {
      u <- Tables.users if u.id === id
      ug <- Tables.userGroup if ug.userId === u.id
      g <- Tables.groups if ug.groupId === g.id
    } yield (u, g)

    // How I understood slick has no way to bind models like m:m (like @Many_To_Many annotation from hibernate)  directly.
    // So we have to linked their manually using something like pivot table, haven't we?

    db.run(user.result).map(t => Some(t.foldLeft(t.head._1, Seq[String]())((pref, next) => {
      (pref._1, pref._2 :+ next._2.name)
    })))
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

  def getUsersWithLimitAndOffset(requirements: (Int, Int)): Future[Seq[User]] = {
    db.run(this.drop(requirements._1).take(requirements._2).result)
  }
}
