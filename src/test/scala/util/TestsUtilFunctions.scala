package util

import org.mockito.Mockito

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait TestsUtilFunctions {

  def reset(mocks: Seq[AnyRef]): Unit = mocks.foreach(Mockito.reset(_))

  def setField[O: ClassTag, FV](obj: O, fieldName: String, fieldValue: FV)(implicit ev: TypeTag[O]): O = {
    import scala.reflect.api.JavaUniverse
    val ru: JavaUniverse = scala.reflect.runtime.universe
    val runtimeMirror: ru.type#Mirror = ru.runtimeMirror(obj.getClass.getClassLoader)
    val instanceMirror: ru.type#InstanceMirror = runtimeMirror.reflect(obj)
    val fieldTerm: ru.type#TermName = ru.newTermName(fieldName)
    val fieldSymbol: ru.type#TermSymbol = ru.typeOf[O].declaration(fieldTerm).asTerm.accessed.asTerm
    val fieldMirror: ru.type#FieldMirror = instanceMirror.reflectField(fieldSymbol)
    fieldMirror.set(fieldValue)
    obj
  }

}
