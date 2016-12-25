package ah.advert.module

import ah.advert.service.advert.{AdvertRoutes, AdvertService}
import ah.advert.service.auth.AuthenticationRoutes
import akka.actor.ActorSystem
import akka.event.Logging._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext

trait RoutesModule {
  lazy val authenticationRoutes: AuthenticationRoutes = wire[AuthenticationRoutes]
  lazy val advertRoutes: AdvertRoutes = wire[AdvertRoutes]

  val routes: Route = authenticationRoutes.routes ~ advertRoutes.routes

  val loggedRoutes = logRequestResult("", InfoLevel) {
    routes
  }

  // dependencies
  implicit val ec: ExecutionContext
  implicit val actorSystem: ActorSystem

  // dependencies
  def advertService: AdvertService


}
