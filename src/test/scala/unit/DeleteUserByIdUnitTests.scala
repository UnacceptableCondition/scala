package unit

import java.sql.Date

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.dao._
import by.itechart.tutorial.service.UserService
import by.itechart.tutorial.util.UtilFunctions.currentTime
import org.mockito.Mockito._
import org.mockito.{ArgumentMatchersSugar, Mockito}
import org.scalatest.freespec.AnyFreeSpec
import unit.utill.RepositoriesTestHelper.stub
import unit.utill.TestHelperAliases
import util.TestsUtilFunctions
import by.itechart.tutorial.dao.JdbcProfilesManager.profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class DeleteUserByIdUnitTests extends AnyFreeSpec with ArgumentMatchersSugar with TestHelperAliases with TestsUtilFunctions {

  // region Test Data
  val testUser: User = User(Some(1), "testName", "testSur", "TestEmail", Date.valueOf("2019-12-05"))
  val testUserToGroup: UserToGroupEntity = UserToGroupEntity(Option.empty, testUser.id.get, Settings.defaultGroupId)
  val testUserActivity: UserActivityEntity = UserActivityEntity(Option.empty, testUser.id.get, Settings.defaultUserIsActive, currentTime)

  implicit val testBd = Database.forConfig("testDatabase");
  // endregion

  "UserService" - {

    "SaveUser." +
      "Save user business logic consist from three DAO actions (1 required and 2 optional): " +
      " 1. Save User entity into Users table (required) " +
      " 2. Save UserToGroupEntity into UserToGroupTable to bind user with default group (optional. Only if Settings.defaultUserIsActive == true)" +
      " 3. Save UserActivityEntity into UserActivityTable (optional. Only if Settings.defaultUserIsActive == true)" - {

      "If Settings.defaultUserIsActive == true - all of three DAO actions should be invoked only once." in {

        val userRep = stub(classOf[uRep])(Seq(userSuccessInsertAction(testUser))).get
        val userToGroupRep = stub(classOf[uTGRep])(Seq(userToGroupSuccessInsertAction(testUserToGroup))).get
        val userActivityRep = stub(classOf[uARep])(Seq(userActivitySuccessInsertAction(testUserActivity))).get

        val repositoriesManger: RepositoriesManager = new RepositoriesManager(
          userToGroupRep,
          mock(classOf[UserToUserRepository]),
          userActivityRep,
          userRep,
          mock(classOf[GroupRepository])
        )

        val userService = new UserService(testBd, repositoriesManger)

        val result: Option[User] = Await.result(userService.saveUser(testUser), 500 millis)
        assert(result.isDefined && result.get == testUser)

        Mockito.verify(userRep, times(1)).insertAction(*[User])
        Mockito.verify(userToGroupRep, times(1)).insertAction(*[UserToGroupEntity])
        Mockito.verify(userActivityRep, times(1)).insertAction(*[UserActivityEntity])
      }
    }
  }

}
