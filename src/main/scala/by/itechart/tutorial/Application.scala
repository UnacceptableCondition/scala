package by.itechart.tutorial

import by.itechart.tutorial.config.ApplicationConfig
import by.itechart.tutorial.web.controller.Server
import com.google.inject.{Guice, Inject, Injector}

object Application {

  val applicationConfig: ApplicationConfig = new ApplicationConfig
  val injector: Injector = Guice.createInjector(
    applicationConfig
  )

  @Inject
  val server: Server = injector.getInstance(classOf[Server])

  def main(args: Array[String]): Unit = {
    server.run()
  }
}
