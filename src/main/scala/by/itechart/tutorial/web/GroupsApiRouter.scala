package by.itechart.tutorial.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, concat, get, onSuccess, pathPrefix, _}
import akka.http.scaladsl.server.Route
import by.itechart.tutorial.dao.{Group, GroupRepository}
import by.itechart.tutorial.service.GroupService
import by.itechart.tutorial.util.const.Constants.MODEL_NOT_FOUND
import javax.inject.Inject

class GroupsApiRouter @Inject()(groupService: GroupService) {

  def getGroupsApiRoutes: Route = route

  import by.itechart.tutorial.util.marshalling.GroupJsonProtocol._

  private val route: Route =
    concat(
      get {
        pathPrefix("groups" / LongNumber) { id =>
          val maybeItem = groupService.getGroupById(id)
          onSuccess(maybeItem) {
            case Some(group) => complete(group)
            case None => complete(MODEL_NOT_FOUND.format("group"))
          }
        }
      },
      delete {
        pathPrefix("groups" / LongNumber) { id =>
          val deleted = groupService.deleteGroupById(id);
          onComplete(deleted) { _ => complete(deleted) }
        }
      },
      post {
        path("groups") {
          entity(as[Group]) { group =>
            val saved = groupService.createGroup(group)
            onComplete(saved) { _ => complete(saved) }
          }
        }
      },
      put {
        path("groups" / LongNumber) { id =>
          entity(as[Group]) { group =>
            val updated = groupService.updateGroupById(id, group)
            onComplete(updated) { _ => complete(updated) }
          }
        }
      }
    )

}
