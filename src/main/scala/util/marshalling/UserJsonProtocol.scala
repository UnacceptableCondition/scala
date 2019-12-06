package util.marshalling

import java.sql.{Date, Timestamp}

import model.User
import spray.json.{DefaultJsonProtocol, JsArray, JsString, JsValue, RootJsonFormat}

object UserJsonProtocol extends DefaultJsonProtocol {

  implicit object UserJsonFormat extends RootJsonFormat[User] {
    def write(user: User): JsArray = {
       JsArray(JsString(user.name), JsString(user.dateOfBirth.toString), JsString(user.dateOfCreation.toString))
    }

    def read(value: JsValue): User = {
      value.asJsObject.getFields("name", "dateOfBirth") match {
        case Seq(JsString(name), JsString(dateOfBirth)) =>
          User(Option.empty, name, Date.valueOf(dateOfBirth), new Timestamp(new java.util.Date().getTime))
      }
    }
  }
}