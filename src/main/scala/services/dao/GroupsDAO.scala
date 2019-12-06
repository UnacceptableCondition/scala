package services.dao

import com.google.inject.Inject
import com.google.inject.name.Named
import model.Group
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupsDAO  @Inject()(@Named("db") db: Database) extends TableQuery(new Groups(_)) {
  def findById(id: Long): Future[Option[Group]] = {
    db.run(this.filter(_.id === id).result).map(_.headOption)
  }

  def create(group: Group): Future[Group] = {
    db.run(this returning this.map(_.id) into ((gr, id) => gr.copy(id = Some(id))) += group)
  }

  def deleteById(id: Long): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }
}
