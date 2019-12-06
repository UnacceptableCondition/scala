package services.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, concat, get, onSuccess, pathPrefix, _}
import akka.http.scaladsl.server.Route
import javax.inject.Inject
import model.Group
import services.dao.GroupsDAO

import scala.concurrent.Future

class GroupsApiRouter @Inject()(groupsDao: GroupsDAO) {

  def getGroupsApiRoutes: Route = route

  import util.marshalling.GroupJsonProtocol._

  private val route: Route =
    concat(
      get {
        pathPrefix("group" / LongNumber) { id =>
          val maybeItem: Future[Option[Group]] = groupsDao.findById(id)
          onSuccess(maybeItem) {
            case Some(group) => complete(group)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },
      delete {
        pathPrefix("group" / LongNumber) { id =>
          onComplete(groupsDao.deleteById(id)) { _ =>
            complete("group deleted")
          }
        }
      },
      post {
        path("createGroup") {
          entity(as[Group]) { group =>
            val saved: Future[Group] = groupsDao.create(group)
            onComplete(saved) { _ =>
              complete("group created")
            }
          }
        }
      }
    )

}
