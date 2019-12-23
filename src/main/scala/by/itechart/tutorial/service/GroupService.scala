package by.itechart.tutorial.service

import by.itechart.tutorial.dao.{Group, RepositoriesManager}
import com.google.inject.Inject

import scala.concurrent.Future

class GroupService @Inject()(repositoriesManager: RepositoriesManager) {

  private val groupRep = repositoriesManager.groupRepository

  def getGroupById(id: Long): Future[Option[Group]] = groupRep.findById(id)

  def saveGroup(group: Group): Future[Option[Group]] = groupRep.insert(group)

  def deleteGroupById(id: Long): Future[Option[Group]] = groupRep.deleteById(id)

  def updateGroupById(id: Long)(group: Group): Future[Option[Group]] = groupRep.update(id, group)
}
