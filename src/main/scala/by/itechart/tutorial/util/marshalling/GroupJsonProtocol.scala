package by.itechart.tutorial.util.marshalling

import by.itechart.tutorial.dao.Group
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsString, JsValue, RootJsonFormat}

object GroupJsonProtocol extends DefaultJsonProtocol {

  implicit object GroupJsonFormat extends RootJsonFormat[Group] {
    def write(group: Group): JsArray =
      JsArray(JsString(group.name))

    def read(value: JsValue): Group = {
      value.asJsObject.getFields("name") match {
        case Seq(JsString(name)) => Group(Option.empty, name)
      }
    }
  }

}
