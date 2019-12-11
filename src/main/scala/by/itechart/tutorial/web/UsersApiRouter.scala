package by.itechart.tutorial.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, onComplete, onSuccess, path, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import by.itechart.tutorial.dao.User
import by.itechart.tutorial.service.UserService
import by.itechart.tutorial.util.const.Constants.MODEL_NOT_FOUND
import by.itechart.tutorial.util.marshalling.UserJsonProtocol
import javax.inject.Inject

import scala.concurrent.Future

class UsersApiRouter @Inject()(userJsonProtocol: UserJsonProtocol, userService: UserService) {

  def getUsersApiRoutes: Route = route

  import userJsonProtocol._

  private val route: Route =
    concat(
      // 5)	Просмотреть детали о конкретном пользователе (за исключением 1:M связей ресурса) . Step 1
      get {
        pathPrefix("users" / LongNumber) { id =>
          val maybeItem: Future[Option[User]] = userService.getUserById(id)
          onSuccess(maybeItem) {
            case Some(user) => complete(user)
            case None => complete(MODEL_NOT_FOUND.format("user"))
          }
        }
      },
      // 8)	Удалить пользователя. Step 1
      delete {
        pathPrefix("users" / LongNumber) { id =>
          val deleted = userService.deleteUserById(id)
          onComplete(deleted) { _ => complete(deleted) }
        }
      },
      // 4)	Создать пользователя. Пользователь создается активным или не активным в зависимости от конфигурации приложения. Step 1
      post {
        path("users") {
          entity(as[User]) { user =>
            val saved: Future[User] = userService.createUser(user)
            onComplete(saved) { user =>
              complete(user)
            }
          }
        }
      },
      // 7)	Обновить представление  пользователя целиком. Step 1
      put {
        path("users" / LongNumber) { id =>
          entity(as[User]) { user =>
            val updated: Future[Option[User]] = userService.updateUserById(id, user)
            onComplete(updated) { _ =>
              complete(updated)
            }
          }
        }
      },
      // 6)	Просмотреть полное представление конкретного пользователя (включая 1:M связи) . Step 1
      get {
        pathPrefix("full" / "users" / LongNumber) { id =>
          val maybeItem = userService.getFullUserInfoById(id)
          onSuccess(maybeItem) {
            case Some(user) => complete(user)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },

      // 2)	Просмотреть указанную страницу пользователей. Максимальный размер страницы – 100. Step 1
      get {
        pathPrefix("users" / "pages" / IntNumber / IntNumber) {
          (offset, limit) => complete(userService.getUsersWitOffsetAndLimit(offset, limit))
        }
      },
      // 1)	Просмотреть всех пользователей. По умолчанию возвращает первую страницу результатов с 20 элементами. Step 1
      get {
        pathPrefix("users" / "pages") {
          complete(userService.getUsersFirstPage)
        }
      },
      // 3)	Просмотреть всех пользователей без пагинации. Step 1
      get {
        pathPrefix("users" / "pages" / "all") {
          complete(userService.getAllUsers)
        }
      }
    )
}
