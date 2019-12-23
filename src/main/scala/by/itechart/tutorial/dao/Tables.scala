package by.itechart.tutorial.dao

import java.sql.{Date, Timestamp}

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.util.UtilFunctions.currentTime
import io.swagger.v3.oas.annotations.media.Schema
import by.itechart.tutorial.dao.JdbcProfilesManager.profile.api._
import io.swagger.v3.oas.annotations.media.Schema.AccessMode

object Tables {
  lazy val users = TableQuery[Users]
  lazy val groups = TableQuery[Groups]
  lazy val userGroup = TableQuery[UserToGroupTable]
  lazy val userToUser = TableQuery[UserToUserTable]
  lazy val userActivityTable = TableQuery[UserActivityTable]
}

class UserToGroupTable(tag: Tag) extends Table[UserToGroupEntity](tag, "user_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def userId = column[Long]("user_id")

  def groupId = column[Long]("group_id")

  def user = foreignKey("user_fk", userId, Tables.users)(_.id)

  def group = foreignKey("group_fk", groupId, Tables.groups)(_.id)

  def * = (id.?, userId, groupId) <> (UserToGroupEntity.tupled, UserToGroupEntity.unapply)
}

class UserToUserTable(tag: Tag) extends Table[UserToUserEntity](tag, "user_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def leftUserId = column[Long]("left_user_id")

  def rightUserId = column[Long]("right_user_id")

  def user = foreignKey("user_fk", leftUserId, Tables.users)(_.id)

  def group = foreignKey("user_fk", rightUserId, Tables.users)(_.id)

  def * = (id.?, leftUserId, rightUserId) <> (UserToUserEntity.tupled, UserToUserEntity.unapply)
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

class UserActivityTable(tag: Tag) extends Table[UserActivityEntity](tag, "user_activity") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def userId = column[Long]("user_id")

  def isActive = column[Boolean]("is_active")

  def lastUpdate = column[Timestamp]("last_update_date")

  def * = (id.?, userId, isActive, lastUpdate) <> (UserActivityEntity.tupled, UserActivityEntity.unapply)
}

case class UserToGroupEntity(id: Option[Long], userId: Long, groupId: Long)

case class UserToUserEntity(id: Option[Long], leftUserId: Long, rightUserId: Long)

case class UserActivityEntity(
                               id: Option[Long] = Option.empty,
                               userId: Long,
                               isActive: Boolean,
                               lastUpdate: Timestamp
                             )

@Schema(
  name = "UserModel",
  description = "Model for user representation",
  requiredProperties = Array("name", "surname", "email", "dateOfBirth")
)
case class User(
                 @Schema(accessMode = AccessMode.READ_ONLY) id: Option[Long] = Option.empty,
                 name: String,
                 surname: String,
                 email: String,
                 dateOfBirth: Date,
                 @Schema(accessMode = AccessMode.READ_ONLY) creationDate: Timestamp = currentTime,
                 @Schema(accessMode = AccessMode.READ_ONLY) lastUpdateTime: Timestamp = currentTime,
                 @Schema(accessMode = AccessMode.READ_ONLY) isActive: Boolean = Settings.defaultUserIsActive
               )

@Schema(
  name = "GroupModel",
  description = "Model for group representation",
  requiredProperties = Array("name")
)
final case class Group(
                        @Schema(accessMode = AccessMode.READ_ONLY) id: Option[Long] = Option.empty,
                        name: String
                      )

