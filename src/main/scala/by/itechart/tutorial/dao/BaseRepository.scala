package by.itechart.tutorial.dao

import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.AbstractTable
import slick.sql.FixedSqlAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

abstract class BaseRepository[T <: AbstractTable[_], I: BaseTypedType](val db: Database) {
  val profile: JdbcProfile
  val defaultPageSize = 20

  import profile.api._

  type Id = I

  def table: TableQuery[T]

  def getId(t: T): Rep[Id]

  def copyWithId(model: T#TableElementType, id: Id): T#TableElementType

  // region API
  def findById(id: Id): Future[Option[T#TableElementType]] = db run filterByIdAction(id).result.headOption

  def findAll(): Future[Seq[T#TableElementType]] = db.run(table.result)

  def findFirstPage(): Future[Seq[T#TableElementType]] = db.run(table.take(defaultPageSize).result)

  def insert(model: T#TableElementType): Future[Option[T#TableElementType]] = {
    db.run(insertAction(model))
  }

  def update(id: Id, model: T#TableElementType): Future[Option[T#TableElementType]] = {
    db run updateAction(id, model)
  }

  def deleteById(id: Id): Future[Option[T#TableElementType]] = {
    findById(id).andThen({
      case model: T#TableElementType => db run deleteAction(id).delete andThen {
        case Success(_) => Some(model)
        case _ => None
      }
      case _ => None
    })
  }

  def findWithOffsetAndLimit(offset: Int, limit: Int): Future[Seq[T#TableElementType]] = {
    db.run(table.drop(offset).take(if (limit > 100 || limit < 0) 100 else limit).result)
  }

  // endregion


  // region Base Actions
  def deleteAction(id: Id): profile.DeleteActionExtensionMethods = {
    profile.createDeleteActionExtensionMethods(
      profile.deleteCompiler.run(filterByIdAction(id).toNode).tree, ()
    )
  }

  def filterByIdAction(id: Id): Query[T, T#TableElementType, Seq] = table filter (getId(_) === id)

  def insertAction(model: T#TableElementType): FixedSqlAction[Some[T#TableElementType], NoStream, Effect.Write] = {
    table returning table.map(getId) into ((m, id) => Some(copyWithId(m, id))) += model
  }

  def updateAction(id: Id, model: T#TableElementType): DBIOAction[Option[T#TableElementType], NoStream, Effect.Write] = {
    filterByIdAction(id).update(copyWithId(model, id)) map {
      case 0 => None
      case _ => Some(copyWithId(model, id))
    }
  }

  // endregion
}
