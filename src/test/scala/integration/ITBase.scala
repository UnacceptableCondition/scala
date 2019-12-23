package integration

import by.itechart.tutorial.dao.{GroupRepository, RepositoriesManager, UserActivityRepository, UserRepository, UserToGroupRepository, UserToUserRepository}
import by.itechart.tutorial.service.UserService
import slick.jdbc.H2Profile
import slick.jdbc.JdbcBackend.Database

trait ITBase {

  val database = Database.forURL(
    url = "jdbc:h2:mem:" + this.hashCode(),
    driver = "org.h2.Driver",
    keepAliveConnection = true
  )
  val profile = H2Profile

  val repositoriesManger: RepositoriesManager = new RepositoriesManager(
    new UserToGroupRepository(database, profile),
    new UserToUserRepository(database, profile),
    new UserActivityRepository(database, profile),
    new UserRepository(database, profile),
    new GroupRepository(database, profile)
  )

  val userService: UserService = new UserService(database, repositoriesManger)

}
