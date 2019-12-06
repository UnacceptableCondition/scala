package services.dao

import java.sql.{Date, Timestamp}

import model.{Group, User, UserGroup}
import slick.jdbc.PostgresProfile.api._

object Tables {
  lazy val users = TableQuery[Users]
  lazy val groups = TableQuery[Groups]
  lazy val userGroup = TableQuery[UserGroupTable]
}

class UserGroupTable(tag: Tag) extends Table[UserGroup](tag, "user_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")
  def groupId = column[Long]("group_id")
  def user = foreignKey("user_fk", userId, Tables.users)(_.id)
  def group = foreignKey("group_fk", groupId, Tables.groups)(_.id)
  def * = (id.?, userId, groupId) <> (UserGroup.tupled, UserGroup.unapply)
}

class Groups(tag: Tag) extends Table[Group](tag, "groups") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def * = (id.?, name) <> (Group.tupled, Group.unapply)
}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def dateOfBirth = column[Date]("date_birth")
  def dateOfCreation = column[Timestamp]("date_creation")
  def * = (id.?, name, dateOfBirth, dateOfCreation) <> (User.tupled, User.unapply)
}
