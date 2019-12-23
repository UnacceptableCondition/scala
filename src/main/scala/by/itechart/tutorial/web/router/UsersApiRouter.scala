package by.itechart.tutorial.web.router

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives.{complete, concat, get, onComplete, onSuccess, post, _}
import akka.http.scaladsl.server.Route
import by.itechart.tutorial.dao.User
import by.itechart.tutorial.service.UserService
import by.itechart.tutorial.util.const.Constants._
import by.itechart.tutorial.web.swagger.SwaggerUserApiDoc
import javax.inject.Inject
import org.json4s.native.Serialization.write

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

class UsersApiRouter @Inject()(userService: UserService) extends BaseRouter[User] with SwaggerUserApiDoc {

  override val apiBase: String = "users"

  def getUsersApiRoutes: Route = route

  implicit def userWithGroupsConversion(x: (User, Seq[String])): ToResponseMarshallable = write(x)

  implicit def usersConversion(x: Seq[User]): ToResponseMarshallable = write(x)

  private val route: Route =
    concat(
      get {
        parameters("mode".?) { mode =>
          pathBaseAndNumber() { id =>
            mode match {
              case Some(v) if v == "full" =>
                onSuccess(userService.getFullUserInfoById(id)) {
                  case Some(user) => complete(user)
                  case None => complete(UserNotFoundExceptionMessage)
                }
              case None => gettingHandler(userService.getUserById(id), UserNotFoundExceptionMessage)
              case _ => complete(InvalidParamsExceptionMessage)
            }
          }
        }
      },
      get {
        parameters("offset".as[Int].?, "limit".as[Int].?) { (offset, limit) =>
          pathBase() {
            completeQuery(
              userService.getUsersWitOffsetAndLimit(offset.getOrElse(0))(limit.getOrElse(0))
            )
          }
        }
      },
      delete {
        pathBaseAndNumber() { id =>
          delintingHandler(userService.deleteUserById(id), DeleteUserExceptionMessage)
        }
      },
      post {
        pathBase() {
          extractStrictEntity(3.seconds) {
            entity => insertingHandler(entity, userService.saveUser, PostUserExceptionMessage)
          }
        }
      },
      put {
        pathBaseAndNumber() {
          id =>
            extractStrictEntity(3.seconds) {
              entity => updatingHandler(entity, userService.updateUserById(id), PutUserExceptionMessage)
            }
        }
      },
      patch {
        parameters("activity".as[Boolean]) { status =>
          pathBaseAndNumber() {
            id =>
              onComplete(userService.changeUserActivityStatus(id, status)) {
                case Success(_) => complete(UserActivityChangeMessage)
                case _ => complete(UserActivityChangeExceptionMessage)
              }
          }
        }
      }
    )

  private def completeQuery(callback: => Future[Seq[User]]): Route = {
    onComplete(callback) {
      case Success(items) => complete(items)
      case _ => complete(UserNotFoundExceptionMessage)
    }
  }
}
