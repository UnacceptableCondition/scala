package by.itechart.tutorial.util

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import org.json4s.{DefaultFormats, Formats}
import org.json4s.native.Serialization.write
import by.itechart.tutorial.util.UtilFunctions.StringToDate

trait JsonConversionsProvider[A] {

  implicit val formats: Formats = DefaultFormats + StringToDate

  implicit def optionModelConversion(x: Option[A]): ToResponseMarshallable = write(x)

  implicit def modelConversion(x: A): ToResponseMarshallable = write(x)

  implicit def exceptionMessageConversionToRM(x: (String, String)): ToResponseMarshallable = write(x)
  implicit def exceptionMessageConversionToString(x: (String, String)): String = write(x)

  implicit def exceptionMessageConversionWithCode(x: (StatusCodes.ClientError, (String, String))): ToResponseMarshallable =
    x._1 -> write(x._2)

}
