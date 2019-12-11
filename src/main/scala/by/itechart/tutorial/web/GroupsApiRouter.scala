package by.itechart.tutorial.web

import akka.http.scaladsl.server.Directives.{complete, concat, get, onSuccess, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import by.itechart.tutorial.dao.Group
import by.itechart.tutorial.service.GroupService
import by.itechart.tutorial.util.const.Constants.{DELETE_GROUP_EXCEPTION_MESSAGE, GROUP_NOT_FOUND, POST_GROUP_EXCEPTION_MESSAGE, PUT_GROUP_EXCEPTION_MESSAGE}
import javax.inject.Inject
import org.json4s.native.Serialization.{read, write}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.duration._
import scala.util.Success

class GroupsApiRouter @Inject()(groupService: GroupService) {

  def getGroupsApiRoutes: Route = route

  implicit val formats: Formats = DefaultFormats

  import by.itechart.tutorial.Application._

  private val route: Route =
    concat(
      get {
        pathPrefix("groups" / LongNumber) { id =>
          val maybeItem = groupService.getGroupById(id)
          onSuccess(maybeItem) {
            case Some(group) => complete(write(group))
            case None => complete(write(GROUP_NOT_FOUND))
          }
        }
      },
      delete {
        pathPrefix("groups" / LongNumber) { id =>
          val deleted = groupService.deleteGroupById(id)
          onComplete(deleted) {
            case Success(group) => complete(write(group))
            case _ => complete(write(DELETE_GROUP_EXCEPTION_MESSAGE))
          }
        }
      },
      post {
        path("groups") {
          extractStrictEntity(3.seconds) { entity =>
            val marshaling = Unmarshal(entity).to[String].map(v => {
              groupService.saveGroup(read[Group](v))
            })
            onComplete(marshaling) {
              case Success(saved) => onComplete(saved) {
                case Success(group) => complete(write(group))
              }
              case _ => complete(write(POST_GROUP_EXCEPTION_MESSAGE))
            }
          }
        }
      },
      put {
        path("groups" / LongNumber) { id =>
          extractStrictEntity(3.seconds) { entity =>
            val marshaling = Unmarshal(entity).to[String].map(v => {
              groupService.updateGroupById(id, read[Group](v))
            })
            onComplete(marshaling) {
              case Success(updated) => onComplete(updated) {
                case Success(group) => complete(write(group))
              }
              case _ => complete(write(PUT_GROUP_EXCEPTION_MESSAGE))
            }
          }
        }
      }
    )
}
