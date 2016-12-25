package ah.advert.service.advert

import java.time.LocalDate

import ah.advert.BaseTestRoutes
import ah.advert.entity.Advert
import ah.advert.entity.Fuel._
import ah.advert.json.JsonProtocol._
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}

class AdvertRoutesSpec extends BaseTestRoutes {

  val testPath = "/advert"

  it should "get empty adverts" in {
    val authHeader: Authorization = getAuthHeader

    Get(testPath) ~> authHeader ~> advertRoutes.routes ~> check {
      responseAs[Seq[Advert]] shouldEqual Seq.empty[Advert]
    }
  }

  it should "create create a new advert with all fields" in {
    val authHeader: Authorization = getAuthHeader

    val advert1 = Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.now))
    val post: HttpRequest = Post(testPath, advert1)

    post ~> authHeader ~> advertRoutes.routes ~> check {
      status === StatusCodes.Created
      responseAs[String] should not be empty
    }

    Get(testPath) ~> authHeader ~> advertRoutes.routes ~> check {
      val str = responseAs[String]
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
    }
  }
}