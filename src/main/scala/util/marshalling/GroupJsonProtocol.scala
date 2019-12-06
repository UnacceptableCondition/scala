package util.marshalling

import model.Group
import spray.json.{DefaultJsonProtocol, JsArray, JsString, JsValue, RootJsonFormat}

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
