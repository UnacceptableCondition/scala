package by.itechart.tutorial

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import by.itechart.tutorial.config.ApplicationConfig
import by.itechart.tutorial.web.controller.Server
import com.google.inject.{Guice, Inject, Injector}

import scala.concurrent.ExecutionContextExecutor

object Application {

  val applicationConfig: ApplicationConfig = new ApplicationConfig
  val injector: Injector = Guice.createInjector(
    applicationConfig
  )

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  @Inject
  val server: Server = injector.getInstance(classOf[Server])

  def main(args: Array[String]): Unit = {
    server.run()
  }
}
