package ah.advert.service.advert

import ah.advert.entity.Advert
import ah.advert.json.JsonProtocol._
import ah.advert.service.auth.{RefreshTokenStorageImpl, SessionData}
import ah.advert.service.common.SessionSupport
import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.CorsDirectives._
import com.softwaremill.macwire._
import com.softwaremill.session.RefreshTokenStorage
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class AdvertRoutes(val advertService: AdvertService)(implicit val ec: ExecutionContext, implicit val actorSystem: ActorSystem)
  extends DefaultJsonProtocol with SprayJsonSupport with SessionSupport {
  val logger = LoggerFactory.getLogger(classOf[AdvertRoutes])

  val basePath = "advert"

  implicit lazy val refreshTokenStorage: RefreshTokenStorage[SessionData] = wire[RefreshTokenStorageImpl]

  val allAdverts: Route =
    cors() {
      path(basePath) {
        get {
          requiredSession(oneOff, usingHeaders) { session =>
            onComplete(advertService.findAll().mapTo[Seq[Advert]]) {
              case Success(advertList) => complete(advertList)
              case Failure(ex) => {
                logger.error("Error requesting latest adverts", ex)
                complete(InternalServerError, s"An error occurred reading latest adverts: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  val getAdvert =
    cors() {
      path(basePath / LongNumber) { (advertId) =>
        get {
          requiredSession(oneOff, usingHeaders) { session =>
            onComplete(advertService.findById(advertId).mapTo[Option[Advert]]) {
              case Success(advertOption) => advertOption match {
                case Some(advert) => complete(advert)
                case None => complete(NotFound, s"The supplier doesn't exist")
              }
              case Failure(ex) => {
                logger.error(s"Error requesting advert with id=$advertId", ex)
                complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }

  val postAdvert =
    cors() {
      path(basePath) {
        post {
          entity(as[Advert]) { advert =>
            requiredSession(oneOff, usingHeaders) { session =>
              onComplete(advertService.insert(advert)) {
                case Success(id) => complete(Created -> id.toString)
                case Failure(ex) => {
                  logger.error(s"Error posting advert $advert", ex)
                  complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
                }
              }
            }
          }
        }
      }
    }

  val routes: Route = allAdverts ~ getAdvert ~ postAdvert

}
