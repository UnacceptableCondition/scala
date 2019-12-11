package by.itechart.tutorial.service

import by.itechart.tutorial.dao.{Group, GroupRepository}
import com.google.inject.Inject

import scala.concurrent.Future

class GroupService @Inject()(groupRepository: GroupRepository) {

  def getGroupById(id: Long): Future[Option[Group]] = groupRepository.findById(id)

  def createGroup(group: Group): Future[Group] = groupRepository.insert(group)

  def deleteGroupById(id: Long): Future[Option[Group]] = groupRepository.deleteById(id)

  def updateGroupById(id: Long, group: Group): Future[Option[Group]] = groupRepository.update(id, group)
}
