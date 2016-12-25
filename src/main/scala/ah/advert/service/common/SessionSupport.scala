package ah.advert.service.common

import ah.advert.entity.SimpleUser
import ah.advert.service.UserService
import ah.advert.service.auth.SessionData
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.softwaremill.macwire._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.softwaremill.session._
import org.json4s._

import scala.concurrent.ExecutionContext

trait SessionSupport {

  val defaultConfig = SessionConfig.default("some_very_long_secret_and_random_string_some_very_long_secret_and_random_string")
  val configMaxAge = defaultConfig.copy(sessionMaxAgeSeconds = Some(3600))
  val configEncryptedMaxAge = configMaxAge.copy(sessionEncryptData = true)

  implicit val serializer: SessionSerializer[SessionData, JValue] = JValueSessionSerializer.caseClass[SessionData]
  implicit val encoder = new JwtSessionEncoder[SessionData]
  implicit val manager = new SessionManager[SessionData](configEncryptedMaxAge)
  val clientManager = manager.clientSessionManager

  implicit def refreshTokenStorage: RefreshTokenStorage[SessionData]

  implicit val ec: ExecutionContext

  val userService: UserService = wire[UserService]

  def userFromSession: Directive1[SimpleUser] = userIdFromSession.flatMap { username =>
    onSuccess(userService.findByUsername(username)).flatMap {
      case None => reject(AuthorizationFailedRejection)
      case Some(user) => provide(user)
    }
  }

  def userIdFromSession: Directive1[String] = session(refreshable, usingCookies).flatMap {
    _.toOption match {
      case None => reject(AuthorizationFailedRejection)
      case Some(s) => provide(s.username)
    }
  }
}
