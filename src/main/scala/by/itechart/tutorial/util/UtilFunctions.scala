package by.itechart.tutorial.util

import java.sql.{Date, Timestamp}
import java.text.SimpleDateFormat

import org.json4s.{CustomSerializer, DefaultFormats}
import org.json4s.JsonAST.JString
import by.itechart.tutorial.util.const.Constants.DATE_FORMAT

object UtilFunctions {
  implicit val formats: DefaultFormats.type = DefaultFormats

  val dateFormat = new SimpleDateFormat(DATE_FORMAT)

  def currentTime: java.sql.Timestamp = new Timestamp(new java.util.Date().getTime)

  object StringToDate extends CustomSerializer[Date](_ => ( {
    case JString(x) => Date.valueOf(x)
  }, {
    case x: Date => JString(dateFormat.format(x))
  }))

}
