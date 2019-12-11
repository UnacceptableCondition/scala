package by.itechart.tutorial.dao

import com.google.inject.Inject
import com.google.inject.name.Named
import slick.jdbc.{JdbcProfile, PostgresProfile}
import slick.jdbc.PostgresProfile.api._


class GroupRepository @Inject()(@Named("db") db: Database) extends Repository[Groups, Long](db) {

  val profile: JdbcProfile = PostgresProfile
  val table = TableQuery[Groups]

  override def getId(t: Groups): profile.api.Rep[Id] = t.id

  override def copyWithId(model: Group, id: Id): Group = model.copy(id = Some(id))
}

final case class Group(id: Option[Long], name: String)


