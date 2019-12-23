package integration

import java.sql.Date

import by.itechart.tutorial.dao._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import util.TestsUtilFunctions

import scala.concurrent.Await
import scala.concurrent.duration._


class ChangeUserActivityStatusIntegrationTests extends AnyFreeSpec with ScalaFutures with TestsUtilFunctions with ITBase {

  import profile.api._
  val testUser = User(Option.empty, "testName1", "testSur1", "TestEmail1", Date.valueOf("2019-12-05"))
  val setup = DBIO.seq(
    (Tables.users.schema ++ Tables.groups.schema ++ Tables.userGroup.schema ++ Tables.userActivityTable.schema).create,
    Tables.groups += Group(Option.empty, "group1"),
    Tables.users += testUser,
    Tables.userGroup += UserToGroupEntity(Option.empty, 1, 1),
    Tables.userActivityTable += UserActivityEntity(Option.empty, 1, true, testUser.lastUpdateTime)
  )
  database.run(setup)

  "UserRepository" - {
    "changeUserActivityStatus" - {
      "If Settings.defaultUserIsActive == true user saving must consists from next 3 actions: " +
        " 1. Add user into userRepository " +
        " 2. Add note about user activity into userActivityRepository " +
        " 3. Add note about user groups into userToGroupRepository to bind user with default group" in {
        Await.result(userService.changeUserActivityStatus(1, false), 500 millis)

        Await.result(database.run(repositoriesManger.userToGroupRepository.filterByUserIdAction(1).result), 500 millis) match {
          case Vector() => assert(true)
          case _ => assert(false, "Note about user groups mustn't existed in userToGroupRepository")
        }

        Await.result(database.run(repositoriesManger.userActivityRepository.filterByUserIdAction(1).result), 500 millis) match {
          case Vector(UserActivityEntity(Some(1), 1, false, _)) => assert(true)
          case _ => assert(false, "Note about user activity mustn't existed in userActivityRepository")
        }
      }
    }
  }
}
