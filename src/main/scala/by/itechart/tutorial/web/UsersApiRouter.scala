package by.itechart.tutorial.web

import akka.http.scaladsl.server.Directives.{complete, concat, get, onComplete, onSuccess, path, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import by.itechart.tutorial.dao.User
import by.itechart.tutorial.service.UserService
import by.itechart.tutorial.util.UtilFunctions.StringToDate
import by.itechart.tutorial.util.const.Constants._
import javax.inject.Inject
import org.json4s.native.Serialization.{read, write}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

class UsersApiRouter @Inject()(userService: UserService) {
  implicit val formats: Formats = DefaultFormats + StringToDate

  import by.itechart.tutorial.Application._

  def getUsersApiRoutes: Route = route

  private val route: Route =
    concat(
      get {
        pathPrefix("users" / LongNumber) { id =>
          val maybeItem: Future[Option[User]] = userService.getUserById(id)
          onSuccess(maybeItem) {
            case Some(user) => complete(write(user))
            case None => complete(write(USER_NOT_FOUND))
          }
        }
      },
      delete {
        pathPrefix("users" / LongNumber) { id =>
          val deleted = userService.deleteUserById(id)
          onComplete(deleted) {
            case Success(user) => complete(write(user))
            case _ => complete(write(DELETE_USER_EXCEPTION_MESSAGE))
          }
        }
      },
      post {
        path("users") {
          extractStrictEntity(3.seconds) { entity =>
            val marshaling = Unmarshal(entity).to[String].map(v => {
              userService.saveUser(read[User](v))
            })
            onComplete(marshaling) {
              case Success(saved) => onComplete(saved) {
                case Success(user) => complete(write(user))
              }
              case _ => complete(write(POST_USER_EXCEPTION_MESSAGE))
            }
          }
        }
      },
      put {
        path("users" / LongNumber) { id =>
          extractStrictEntity(3.seconds) { entity =>
            val marshaling = Unmarshal(entity).to[String].map(v => {
              userService.updateUserById(id, read[User](v))
            })
            onComplete(marshaling) {
              case Success(updated) => onComplete(updated) {
                case Success(user) => complete(write(user))
              }
              case _ => complete(write(PUT_USER_EXCEPTION_MESSAGE))
            }
          }
        }
      },
      get {
        pathPrefix("full" / "users" / LongNumber) { id =>
          val maybeItem = userService.getFullUserInfoById(id)
          onSuccess(maybeItem) {
            case Some(user) => complete(write(user))
            case None => complete(write(USER_NOT_FOUND))
          }
        }
      },
      get {
        pathPrefix("users" / "pages" / IntNumber / IntNumber) {
          (offset, limit) => complete(write(userService.getUsersWitOffsetAndLimit(offset, limit)))
        }
      },
      get {
        pathPrefix("users" / "pages") {
          complete(write(userService.getUsersFirstPage))
        }
      },
      get {
        pathPrefix("users" / "pages" / "all") {
          complete(write(userService.getAllUsers))
        }
      }
    )
}
