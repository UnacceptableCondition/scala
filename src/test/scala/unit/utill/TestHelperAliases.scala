package unit.utill

import by.itechart.tutorial.dao._
import unit.utill.RepositoriesTestHelper.stubInsertActionSuccess
import by.itechart.tutorial.dao.JdbcProfilesManager.profile.api._

trait TestHelperAliases {

  type uRep = UserRepository
  type uTGRep = UserToGroupRepository
  type gRep = GroupRepository
  type uARep = UserActivityRepository

  // region success input action aliases
  def userSuccessInsertAction: User => uRep => uRep = stubInsertActionSuccess[Users, Long, uRep]

  def userToGroupSuccessInsertAction: UserToGroupEntity => uTGRep => uTGRep = stubInsertActionSuccess[UserToGroupTable, Long, uTGRep]

  def groupSuccessInsertAction: Group => gRep => gRep = stubInsertActionSuccess[Groups, Long, gRep]

  def userActivitySuccessInsertAction: UserActivityEntity => uARep => uARep = stubInsertActionSuccess[UserActivityTable, Long, uARep]

  // endregion
}
