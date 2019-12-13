package by.itechart.tutorial.dao

import com.google.inject.Inject
import com.google.inject.name.Named
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.AccessMode
import slick.jdbc.{JdbcProfile, PostgresProfile}
import slick.jdbc.PostgresProfile.api._


class GroupRepository @Inject()(@Named("db") db: Database) extends Repository[Groups, Long](db) {

  val profile: JdbcProfile = PostgresProfile
  val table = TableQuery[Groups]

  override def getId(t: Groups): profile.api.Rep[Id] = t.id

  override def copyWithId(model: Group, id: Id): Group = model.copy(id = Some(id))
}

@Schema(
  name = "GroupModel",
  description = "Model for group representation",
  requiredProperties = Array("name")
)
final case class Group(
                        @Schema(accessMode = AccessMode.READ_ONLY) id: Option[Long] = Option.empty,
                        name: String
                      )


