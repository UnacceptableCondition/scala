package by.itechart.tutorial.util.marshalling

import java.sql.{Date, Timestamp}

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.dao.User
import com.google.inject.Inject
import spray.json.{DefaultJsonProtocol, JsArray, JsBoolean, JsString, JsValue, RootJsonFormat}

class UserJsonProtocol @Inject()(setting: Settings) extends DefaultJsonProtocol {

  implicit object UserJsonFormat extends RootJsonFormat[User] {
    def write(user: User): JsArray = {
      JsArray(
        JsString(user.name),
        JsString(user.surname),
        JsString(user.email),
        JsString(user.dateOfBirth.toString),
        JsString(user.creationDate.toString),
        JsString(user.lastUpdateTime.toString),
        JsBoolean(user.isActive)
      )
    }

    def read(value: JsValue): User = {
      value.asJsObject.getFields("name", "surname", "email", "dateOfBirth") match {
        case Seq(
        JsString(name),
        JsString(surname),
        JsString(email),
        JsString(dateOfBirth)
        ) =>
          User(
            Option.empty,
            name,
            surname,
            email,
            Date.valueOf(dateOfBirth),
            new Timestamp(new java.util.Date().getTime),
            new Timestamp(new java.util.Date().getTime),
            setting.defaultUserIsActive
          )
      }
    }
  }

}