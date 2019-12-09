package services.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, onComplete, onSuccess, path, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import javax.inject.Inject
import model.User
import services.dao.UsersDAO
import util.marshalling.UserJsonProtocol

import scala.concurrent.Future

class UsersApiRouter @Inject()(usersDao: UsersDAO, userJsonProtocol: UserJsonProtocol) {

  def getUsersApiRoutes: Route = route
  import userJsonProtocol._

  private val route: Route =
    concat(
      get {
        pathPrefix("user" / LongNumber) { id =>
          val maybeItem: Future[Option[User]] = usersDao.findById(id)
          onSuccess(maybeItem) {
            case Some(user) => complete(user)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },
      delete {
        pathPrefix("user" / LongNumber) { id =>
          onComplete(usersDao.deleteById(id)) { _ =>
            complete("user deleted")
          }
        }
      },
      post {
        path("user") {
          entity(as[User]) { user =>
            val saved: Future[User] = usersDao.create(user)
            onComplete(saved) { _ =>
              complete("user created")
            }
          }
        }
      },

      get {
        pathPrefix("userFull" / LongNumber) { id =>
          val maybeItem: Future[Option[(User, Seq[String])]] = usersDao.findByIdFull(id)
          onSuccess(maybeItem) {
            case Some(user) => complete(user)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },

      get {
        pathPrefix("users" / IntNumber / IntNumber) {
          (limit, offset) => complete(usersDao.getUsersWithLimitAndOffset((offset, limit)))
        }
      },
      get {
        pathPrefix("users") {
          complete(usersDao.getUsers)
        }
      },
      get {
        pathPrefix("allUsers") {
          complete(usersDao.getAllUsers)
        }
      }
    )
}
