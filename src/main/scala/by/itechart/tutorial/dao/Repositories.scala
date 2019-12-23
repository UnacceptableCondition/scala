package by.itechart.tutorial.dao

import by.itechart.tutorial.dao.JdbcProfilesManager.profile.api._
import by.itechart.tutorial.util.UtilFunctions.currentTime
import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile
import slick.sql.FixedSqlAction

@Singleton
class RepositoriesManager @Inject()(
                                     val userToGroupRepository: UserToGroupRepository,
                                     val userToUserRepository: UserToUserRepository,
                                     val userActivityRepository: UserActivityRepository,
                                     val userRepository: UserRepository,
                                     val groupRepository: GroupRepository
                                   ) {
}

class UserToGroupRepository @Inject()(
                                       @Named("db") val db: Database,
                                       @Named("profile") val profile: JdbcProfile
                                     ) extends BaseRepository[UserToGroupTable, Long] {

  def table: profile.api.TableQuery[UserToGroupTable] = TableQuery[UserToGroupTable]

  def getId(t: UserToGroupTable): profile.api.Rep[Id] = t.id

  def copyWithId(model: UserToGroupEntity, id: Id): UserToGroupEntity = model.copy(id = Some(id))

  def filterByUserIdAction(id: Long): Query[UserToGroupTable, UserToGroupEntity, Seq] = table filter (_.userId === id)

  def pathAction(userId: Long, groupId: Long): FixedSqlAction[Int, NoStream, Effect.Write] = {
    table insertOrUpdate (UserToGroupEntity(Option.empty, userId, groupId))
  }
}

class UserToUserRepository @Inject()(
                                      @Named("db") val db: Database,
                                      @Named("profile") val profile: JdbcProfile
                                    ) extends BaseRepository[UserToUserTable, Long] {

  def table: profile.api.TableQuery[UserToUserTable] = TableQuery[UserToUserTable]

  def getId(t: UserToUserTable): profile.api.Rep[Id] = t.id

  def copyWithId(model: UserToUserEntity, id: Id): UserToUserEntity = model.copy(id = Some(id))

  def filterByUserIdAction(id: Long): Query[UserToUserTable, UserToUserEntity, Seq] = table filter (_.leftUserId === id)
}

class UserActivityRepository @Inject()(
                                        @Named("db") val db: Database,
                                        @Named("profile") val profile: JdbcProfile
                                      ) extends BaseRepository[UserActivityTable, Long] {

  def table: profile.api.TableQuery[UserActivityTable] = TableQuery[UserActivityTable]

  def getId(t: UserActivityTable): profile.api.Rep[Id] = t.id

  def copyWithId(model: UserActivityEntity, id: Id): UserActivityEntity = model.copy(id = Some(id))

  def filterByUserIdAction(id: Long): Query[UserActivityTable, UserActivityEntity, Seq] = table filter (_.userId === id)
}

class UserRepository @Inject()(
                                @Named("db") val db: Database,
                                @Named("profile") val profile: JdbcProfile
                              ) extends BaseRepository[Users, Long] {

  def table: profile.api.TableQuery[Users] = TableQuery[Users]

  def getId(t: Users): profile.api.Rep[Id] = t.id

  def copyWithId(model: User, id: Id): User = model.copy(id = Some(id))

  def findByIdFullInfoAction(id: Id): Query[(Users, Rep[Option[Groups]]), (User, Option[Group]), Seq] = {
    for {
      ((u, _), g) <- Tables.users joinLeft Tables.userGroup on (_.id === _.userId) joinLeft Tables.groups on (_._2.map(_.groupId) === _.id)
      if u.id === id
    } yield (u, g)
  }

  def patchUserActivityAction(id: Long, activity: UserActivityEntity, status: Boolean): FixedSqlAction[Int, NoStream, Effect.Write] = {
    Tables.userActivityTable.filter(_.userId === id)
      .update(activity.copy(activity.id, activity.userId, status, currentTime))
  }
}

class GroupRepository @Inject()(
                                 @Named("db") val db: Database,
                                 @Named("profile") val profile: JdbcProfile
                               ) extends BaseRepository[Groups, Long] {

  def table: profile.api.TableQuery[Groups] = TableQuery[Groups]

  override def getId(t: Groups): profile.api.Rep[Id] = t.id

  override def copyWithId(model: Group, id: Id): Group = model.copy(id = Some(id))
}
