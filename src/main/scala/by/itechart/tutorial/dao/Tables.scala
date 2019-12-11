package by.itechart.tutorial.dao

import java.sql.{Date, Timestamp}

import slick.jdbc.PostgresProfile.api._

object Tables {
  lazy val users = TableQuery[Users]
  lazy val groups = TableQuery[Groups]
  lazy val userGroup = TableQuery[UserGroupTable]
  lazy val userToUser = TableQuery[UserToUserTable]
}

class UserGroupTable(tag: Tag) extends Table[UserToGroup](tag, "user_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def userId = column[Long]("user_id")

  def groupId = column[Long]("group_id")

  def user = foreignKey("user_fk", userId, Tables.users)(_.id)

  def group = foreignKey("group_fk", groupId, Tables.groups)(_.id)

  def * = (id.?, userId, groupId) <> (UserToGroup.tupled, UserToGroup.unapply)
}

class UserToUserTable(tag: Tag) extends Table[UserToUser](tag, "user_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def leftUserId = column[Long]("left_user_id")

  def rightUserId = column[Long]("right_user_id")

  def user = foreignKey("user_fk", leftUserId, Tables.users)(_.id)

  def group = foreignKey("user_fk", rightUserId, Tables.users)(_.id)

  def * = (id.?, leftUserId, rightUserId) <> (UserToUser.tupled, UserToUser.unapply)
}

class Groups(tag: Tag) extends Table[Group](tag, "groups") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def * = (id.?, name) <> (Group.tupled, Group.unapply)
}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def surname = column[String]("surname")

  def email = column[String]("email")

  def dateOfBirth = column[Date]("date_birth")

  def creationDate = column[Timestamp]("date_creation")

  def lustUpdateDate = column[Timestamp]("last_update_date")

  def isActive = column[Boolean]("is_active")

  def * = (id.?, name, surname, email, dateOfBirth, lustUpdateDate, creationDate, isActive) <> (User.tupled, User.unapply)
}

case class UserToGroup(id: Option[Long], userId: Long, groupId: Long)

case class UserToUser(id: Option[Long], leftUserId: Long, rightUserId: Long)
