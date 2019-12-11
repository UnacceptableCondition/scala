package by.itechart.tutorial.service

import by.itechart.tutorial.dao.{User, UserRepository}
import com.google.inject.Inject

import scala.concurrent.Future

class UserService @Inject()(userRepository: UserRepository) {

  def getUserById(id: Long): Future[Option[User]] = userRepository.findById(id)

  def createUser(user: User): Future[User] = userRepository.insert(user)

  def deleteUserById(id: Long): Future[Option[User]] = userRepository.deleteById(id)

  def updateUserById(id: Long, user: User): Future[Option[User]] = userRepository.update(id, user)

  def getAllUsers: Future[Seq[User]] = userRepository.findAll()

  def getUsersWitOffsetAndLimit(offset: Int, limit: Int): Future[Seq[User]] = userRepository.findWithOffsetAndLimit(offset, limit)

  def getFullUserInfoById(id: Long): Future[Option[(User, Seq[String])]] = userRepository.findByIdFull(id)

  def getUsersFirstPage: Future[Seq[User]] = userRepository.findFirstPage()

}
