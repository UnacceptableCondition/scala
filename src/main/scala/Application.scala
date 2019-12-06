import com.google.inject.{Guice, Inject, Injector}
import config.ApplicationConfig
import modules.Server

object Application{

  val injector: Injector = Guice.createInjector(
      new ApplicationConfig
  )

  @Inject
  val server: Server = injector.getInstance(classOf[Server])

  def main(args: Array[String]): Unit = {
    server.run()
  }
}
