package by.itechart.tutorial.web.swagger

import by.itechart.tutorial.config.Settings
import by.itechart.tutorial.web.router.{GroupsApiRouter, UsersApiRouter}
import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info


object SwaggerConfig extends SwaggerHttpService {
  override val apiClasses: Set[Class[_]] = Set(classOf[UsersApiRouter], classOf[GroupsApiRouter])
  override val host = s"${Settings.swaggerHost}:${Settings.serverPort}"
  override val apiDocsPath: String = Settings.swaggerApiDocsPath
  override val info: Info = Info()
}