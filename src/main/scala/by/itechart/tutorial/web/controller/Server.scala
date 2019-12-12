package by.itechart.tutorial.web.controller

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.web.{GroupsApiRouter, UsersApiRouter}
import com.google.inject.Inject
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.io.StdIn

class Server @Inject()(usersRouter: UsersApiRouter, groupsRouter: GroupsApiRouter) {

  import by.itechart.tutorial.Application._

  val logger: Logger = Logger(classOf[Server])

  val route: Route =
    concat(
      usersRouter.getUsersApiRoutes,
      groupsRouter.getGroupsApiRoutes
    )

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, Settings.serverHost, Settings.serverPort)

  def run(): Unit = {
    logger.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
