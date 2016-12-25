package ah.advert.service.advert

import java.time.LocalDate

import ah.advert.BaseTestRoutes
import ah.advert.entity.{Advert, FuelEnum}
import ah.advert.json.JsonProtocol._
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import ah.advert.entity.FuelEnum._

class AdvertRoutesSpec extends BaseTestRoutes {

  val path = "/advert"

  it should "get latest projects" in {
    val authHeader: Authorization = getAuthHeader

    Get("/project") ~> authHeader ~> advertRoutes.routes ~> check {
      responseAs[Seq[Advert]] shouldEqual Seq.empty[Advert]
    }
  }

  it should "create create a new advert with all fields" in {
    val authHeader: Authorization = getAuthHeader

    val advert1 = Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.now))
    val post: HttpRequest = Post(path, advert1)

    post ~> advertRoutes.routes ~> check {
      status === StatusCodes.Created
      responseAs[String] should not be empty
    }

    Get(path) ~> authHeader ~> advertRoutes.routes ~> check {
      val str = responseAs[String]
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
    }

  }


}