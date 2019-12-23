package by.itechart.tutorial.web.router

import akka.http.scaladsl.model.{HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, onSuccess, path, pathPrefix, _}
import akka.http.scaladsl.server.{Directive, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import by.itechart.tutorial.util.JsonConversionsProvider
import by.itechart.tutorial.util.const.Constants.DefaultExceptionMessage
import com.typesafe.scalalogging.Logger
import org.json4s.native.Serialization.read

import scala.concurrent.Future
import scala.util.{Failure, Success}

abstract class BaseRouter[A: Manifest] extends JsonConversionsProvider[A] {

  import by.itechart.tutorial.Application._

  val logger: Logger = Logger(classOf[BaseRouter[A]])

  type Result = Future[Option[A]]


  val apiBase: String

  // region API
  def pathBaseAndNumber(): Directive[Tuple1[Long]] = pathPrefix(apiBase / LongNumber)

  def pathBase(): Directive[Unit] = path(apiBase)

  def gettingHandler(result: Result, exceptionMessage: (String, String)): Route = completeQuery(result, exceptionMessage)

  def delintingHandler(result: Result, exceptionMessage: (String, String)): Route = completeQuery(result, exceptionMessage)

  def insertingHandler(entity: HttpEntity.Strict, unmarshalCallback: A => Result, exceptionMessage: (String, String)): Route =
    completeQueryWithUnmarshal(unmarshal(entity, unmarshalCallback), exceptionMessage)

  def updatingHandler(entity: HttpEntity.Strict, unmarshalCallback: A => Result, exceptionMessage: (String, String)): Route =
    completeQueryWithUnmarshal(unmarshal(entity, unmarshalCallback), exceptionMessage)

  // endregion

  private def completeQueryWithUnmarshal(future: => Future[Result], exceptionMessage: (String, String)): Route = {
    onComplete(future) {
      case Success(result) => onComplete(result) {
        case Success(result) => complete(result)
        case Failure(exception) =>
          logger.error(exception.getMessage)
          complete(DefaultExceptionMessage)
      }
      case _ => complete(StatusCodes.BadRequest -> exceptionMessage)
    }
  }

  private def unmarshal(entity: HttpEntity.Strict, unmarshalCallback: A => Result): Future[Result] = {
    Unmarshal(entity).to[String].map(v => {
      unmarshalCallback(read[A](v))
    })
  }

  private def completeQuery(result: Result, exceptionMessage: (String, String)): Route = {
    onSuccess(result) {
      case Some(obj) => complete(obj)
      case None => complete(StatusCodes.NotFound -> exceptionMessage)
    }
  }

}
