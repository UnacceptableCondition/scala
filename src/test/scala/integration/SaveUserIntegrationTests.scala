package integration

import java.sql.Date

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.dao._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import slick.dbio.{DBIO => _, DBIOAction => _, Effect => _, NoStream => _}
import util.TestsUtilFunctions

import scala.concurrent.Await
import scala.concurrent.duration._

class SaveUserIntegrationTests extends AnyFreeSpec with ScalaFutures with TestsUtilFunctions with ITBase {

  import profile.api._

  val setup = DBIO.seq(
    (Tables.users.schema ++ Tables.groups.schema ++ Tables.userGroup.schema ++ Tables.userActivityTable.schema).create,
    Tables.groups += Group(Option.empty, "group1")
  )
  database.run(setup)

  "UserRepository" - {
    "saveUser" - {
      "If Settings.defaultUserIsActive == true user saving must consists from next 3 actions: " +
        " 1. Add user into userRepository " +
        " 2. Add note about user activity into userActivityRepository " +
        " 3. Add note about user groups into userToGroupRepository to bind user with default group" in {
        if (!Settings.defaultUserIsActive) setField(Settings, "defaultUserIsActive", true)

        val testUser: User = User(Option.empty, "testName1", "testSur1", "TestEmail1", Date.valueOf("2019-12-05"))

        val userOption = Await.result(userService.saveUser(testUser), 500 millis)
        userOption.map({
          case User(_, testUser.name, testUser.surname, testUser.email, testUser.dateOfBirth, _, _, Settings.defaultUserIsActive) => assert(true)
          case _ => assert(false, "Save method should return saved user with id")
        })

        val user = userOption.get
        val userId = user.id.get
        Await.result(database.run(repositoriesManger.userToGroupRepository.filterByUserIdAction(userId).result), 500 millis) match {
          case Vector(UserToGroupEntity(_, userId, Settings.defaultGroupId)) => assert(true)
          case _ => assert(false, "Note about user groups should be existed in userToGroupRepository")
        }

        Await.result(database.run(repositoriesManger.userActivityRepository.filterByUserIdAction(userId).result), 500 millis) match {
          case Vector(UserActivityEntity(_, userId, Settings.defaultUserIsActive, user.lastUpdateTime)) => assert(true)
          case _ => assert(false, "Note about user activity should be existed in userActivityRepository")
        }

        Await.result(userService.getUserById(userId), 500 millis) match {
          case Some(User(_, testUser.name, testUser.surname, testUser.email, testUser.dateOfBirth, _, _, Settings.defaultUserIsActive)) => assert(true)
          case _ => assert(false, "User should be saved into storage")
        }
      }

      "If Settings.defaultUserIsActive == false user saving must consists from next 2 actions : " +
        " 1. Add user into userRepository " +
        " 2. Add note about user activity into userActivityRepository " in {
        if (Settings.defaultUserIsActive) setField(Settings, "defaultUserIsActive", false)
        val testUser: User = User(Option.empty, "testName2", "testSur2", "TestEmail2", Date.valueOf("2019-12-05"))

        val userOption = Await.result(userService.saveUser(testUser), 500 millis)
        userOption.map({
          case User(_, testUser.name, testUser.surname, testUser.email, testUser.dateOfBirth, _, _, Settings.defaultUserIsActive) => assert(true)
          case x => assert(false, "Save method should return saved user with id")
        })

        val user = userOption.get
        val userId = user.id.get
        Await.result(database.run(repositoriesManger.userToGroupRepository.filterByUserIdAction(userId).result), 500 millis) match {
          case Vector() => assert(true)
          case x => assert(false, "Note about user groups should not be existed in userToGroupRepository")
        }

        Await.result(database.run(repositoriesManger.userActivityRepository.filterByUserIdAction(userId).result), 500 millis) match {
          case Vector(UserActivityEntity(_, userId, Settings.defaultUserIsActive, user.lastUpdateTime)) => assert(true)
          case x => assert(false, "Note about user activity should be existed in userActivityRepository")
        }

        Await.result(userService.getUserById(userId), 500 millis) match {
          case Some(User(_, testUser.name, testUser.surname, testUser.email, testUser.dateOfBirth, _, _, Settings.defaultUserIsActive)) => assert(true)
          case _ => assert(false, "User should be saved into storage")
        }
      }
    }
  }
}
