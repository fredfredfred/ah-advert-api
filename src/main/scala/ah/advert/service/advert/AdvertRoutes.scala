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
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class AdvertRoutes(val advertService: AdvertService)(implicit val ec: ExecutionContext, implicit val actorSystem: ActorSystem)
  extends DefaultJsonProtocol with SprayJsonSupport with SessionSupport {
  private val logger = LoggerFactory.getLogger(classOf[AdvertRoutes])

  val basePath = "advert"

  implicit lazy val refreshTokenStorage: RefreshTokenStorage[SessionData] = wire[RefreshTokenStorageImpl]


  def createOrUpdateAdvert(advertId: Long, advert: Advert): Route = {
    if (advertId != advert.id) {
      complete(BadRequest, s"ids of request and data do not match: request id=${advertId} data id=${advert.id}")
    }
    onComplete(advertService.findById(advertId).mapTo[Option[Advert]]) {
      case Success(advertOption) => advertOption match {
        case Some(foundAdvert) => updateAdvert(advert)
        case None => createAdvert(advert)
      }
      case Failure(ex) => {
        logger.error(s"Error updating advert with id=$advertId", ex)
        complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
    }
  }

  def updateAdvert(advert: Advert): Route = onComplete(advertService.update(advert).mapTo[Int]) {
    case Success(id) => complete(NoContent) // http 204
    case Failure(ex) => {
      logger.error(s"Error updating advert with id=${advert.id}", ex)
      complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
    }
  }

  def createAdvert(advert: Advert): Route = onComplete(advertService.insert(advert)) {
    case Success(id) => {
      complete(Created -> id.toString)
    }
    case Failure(ex) => {
      logger.error(s"Error posting advert $advert", ex)
      complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
    }
  }


  val allAdvertsRoute: Route =
    cors() {
      path(basePath) {
        get {
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

  val getAdvertRoute: Route =
    cors() {
      path(basePath / LongNumber) { (advertId) =>
        get {
          onComplete(advertService.findById(advertId).mapTo[Option[Advert]]) {
            case Success(advertOption) => advertOption match {
              case Some(advert) => complete(advert)
              case None => complete(NotFound, s"The advert doesn't exist")
            }
            case Failure(ex) => {
              logger.error(s"Error requesting advert with id=$advertId", ex)
              complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }

  val createAdvertRoute: Route =
    cors() {
      path(basePath) {
        post {
          entity(as[Advert]) {
            advert => createAdvert(advert)
          }
        }
      }
    }

  val updateAdvertRoute: Route =
    cors() {
      path(basePath / LongNumber) { (advertId) =>
        put {
          entity(as[Advert]) { advert => createOrUpdateAdvert(advertId, advert)
          }
        }
      }
    }

  val deleteAdvertRoute: Route =
    cors() {
      path(basePath / LongNumber) { (advertId) =>
        delete {
          onComplete(advertService.deleteById(advertId).mapTo[Int]) {
            case Success(rowCount) => rowCount match {
              case 0 => complete(NotFound, s"The advert doesn't exist")
              case 1 => complete(NoContent)
              case _ => complete(BadRequest, s"More than one advert has been deleted => This must not happen!")
            }
            case Failure(ex) => {
              logger.error(s"Error deleting advert with id=$advertId", ex)
              complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }


  val routes: Route = allAdvertsRoute ~ getAdvertRoute ~ createAdvertRoute ~ updateAdvertRoute ~ deleteAdvertRoute


}
