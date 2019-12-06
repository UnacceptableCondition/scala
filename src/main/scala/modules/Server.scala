package modules

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.Inject
import services.web.{GroupsApiRouter, UsersApiRouter}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

class Server @Inject() (usersRouter: UsersApiRouter, groupsRouter: GroupsApiRouter) {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route =
    concat(
      usersRouter.getUsersApiRoutes,
      groupsRouter.getGroupsApiRoutes
    )

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

  def run(): Unit = {
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
