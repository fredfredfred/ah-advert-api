package ah.advert.service.auth

import ah.advert.entity.SimpleUser
import ah.advert.json.JsonProtocol._
import ah.advert.service.common.SessionSupport
import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.AuthorizationFailedRejection
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.CorsDirectives._
import com.softwaremill.macwire._
import com.softwaremill.session.RefreshTokenStorage
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContext, Future}

case class LoginRequest(email: String, password: String, rememberMe: Option[Boolean])

object LoginRequest {
}

case class AuthResponse(message: String)

class AuthenticationRoutes(implicit val ec: ExecutionContext, implicit val actorSystem: ActorSystem)
  extends DefaultJsonProtocol with SprayJsonSupport with SessionSupport {

  implicit lazy val refreshTokenStorage: RefreshTokenStorage[SessionData] = wire[RefreshTokenStorageImpl]

  val authRoute =
    cors() {
      path("auth") {
        post {
          entity(as[LoginRequest]) { loginRequest =>
            val authenticatedUser: Future[Option[SimpleUser]] = userService.authenticate(loginRequest.email, loginRequest.password)
            onSuccess(authenticatedUser) {
              case None => reject(AuthorizationFailedRejection)
              case Some(user) =>
                val sessionData = SessionData(user.email)
                (
                  if (loginRequest.rememberMe.getOrElse(false)) {
                    setSession(refreshable, usingHeaders, sessionData)
                  }
                  else {
                    setSession(oneOff, usingHeaders, sessionData)
                  }) {
                  complete(AuthResponse(clientManager.encode(sessionData)))
                }
            }
          }
        }
      }
    }

  val logout =
    cors() {
      path("logout") {
        get {
          invalidateSession(oneOff, usingHeaders) {
            complete {
              "logged out"
            }
          }
        }
      }
    }

  val routes = authRoute ~ logout

}

