package by.itechart.tutorial.web.router

import akka.http.scaladsl.server.Directives.{concat, get, _}
import akka.http.scaladsl.server.Route
import by.itechart.tutorial.dao.Group
import by.itechart.tutorial.service.GroupService
import by.itechart.tutorial.util.const.Constants.{DeleteGroupExceptionMessage, GroupNotFoundExceptionMessage, PostGroupExceptionMessage, PutGroupExceptionMessage}
import javax.inject.Inject

import scala.concurrent.duration._

class GroupsApiRouter @Inject()(groupService: GroupService) extends BaseRouter[Group] {

  def getGroupsApiRoutes: Route = route

  val apiBase: String = "groups"

  private val route: Route =
    concat(
      get {
        pathBaseAndNumber() { id =>
          gettingHandler(groupService.getGroupById(id), GroupNotFoundExceptionMessage)
        }
      },
      delete {
        pathBaseAndNumber() { id =>
          delintingHandler(groupService.deleteGroupById(id), DeleteGroupExceptionMessage)
        }
      },
      post {
        pathBase() {
          extractStrictEntity(3.seconds) {
            entity => insertingHandler(entity, groupService.saveGroup, PostGroupExceptionMessage)
          }
        }
      },
      put {
        pathBaseAndNumber() {
          id =>
            extractStrictEntity(3.seconds) {
              entity => updatingHandler(entity, groupService.updateGroupById(id), PutGroupExceptionMessage)
            }
        }
      }
    )
}
