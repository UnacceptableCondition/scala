package unit.utill

import by.itechart.tutorial.dao.BaseRepository
import org.mockito.{ArgumentMatchersSugar, Mockito}
import slick.ast.BaseTypedType
import slick.lifted.AbstractTable
import by.itechart.tutorial.dao.JdbcProfilesManager.profile.api._


object RepositoriesTestHelper extends ArgumentMatchersSugar{

  def stub[T](value: Class[T])(stubActions: Seq[T => T]): Option[T] = {
    Some(Mockito.mock(value)).map(v => stubActions.foldLeft(v)((acc, next) => next.apply(acc)))
  }

  def stubInsertActionSuccess[
    A <: AbstractTable[_],
    B: BaseTypedType,
    C <: BaseRepository[A, B]
  ] (value: A#TableElementType)(rep: C): C = {
    (Mockito.when(rep.insertAction (*[A#TableElementType])).thenReturn(DBIO.successful(Some(value))))
    rep
  }

  def stubDeleteActionSuccess[
    A <: AbstractTable[_],
    B: BaseTypedType,
    C <: BaseRepository[A, B]
  ] (rep: C): C = {
    (Mockito.when(rep.deleteAction (*[rep.Id])).thenReturn(DBIO.successful(1)))
    rep
  }
}
