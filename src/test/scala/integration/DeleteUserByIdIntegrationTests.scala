package integration

import java.sql.Date

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.dao._
import by.itechart.tutorial.service.UserService
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import slick.jdbc.H2Profile
import slick.jdbc.JdbcBackend.Database
import util.TestsUtilFunctions

import scala.concurrent.Await
import scala.concurrent.duration._

class DeleteUserByIdIntegrationTests extends AnyFreeSpec with ScalaFutures with TestsUtilFunctions with ITBase {

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
    "deleteUserById" - {
      "User deleting must consists from next 3 actions: " +
        " 1. Delete user from userRepository " +
        " 2. Delete all notes about user activity from userActivityRepository " +
        " 3.  Delete all notes about user groups from userToGroupRepository" in {
        val userOption = Await.result(userService.deleteUserById(1), 500 millis)
        userOption.map({
          case User(_, testUser.name, testUser.surname, testUser.email, testUser.dateOfBirth, _, _, _) => assert(true)
          case x => {
            println(x)
            assert(false, "Delete method should return deleted user with id")
          }
        })

        val user = userOption.get
        val userId = user.id.get
        Await.result(database.run(repositoriesManger.userToGroupRepository.filterByUserIdAction(userId).result), 500 millis) match {
          case Vector() => assert(true)
          case _ => assert(false, "Note about user groups mustn't existed in userToGroupRepository")
        }

        Await.result(database.run(repositoriesManger.userActivityRepository.filterByUserIdAction(userId).result), 500 millis) match {
          case Vector() => assert(true)
          case _ => assert(false, "Note about user activity mustn't existed in userActivityRepository")
        }

        Await.result(userService.getUserById(userId), 500 millis) match {
          case None => assert(true)
          case _ => assert(false, "User should be absent in storage")
        }
      }
    }
  }

}
