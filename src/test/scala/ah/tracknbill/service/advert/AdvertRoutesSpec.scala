package ah.tracknbill.service.advert

import java.time.LocalDate

import ah.tracknbill.BaseTestRoutes
import ah.tracknbill.entity.{Advert, Project}
import ah.tracknbill.json.JsonProtocol._
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}

class AdvertRoutesSpec extends BaseTestRoutes {

  val path = "/advert"

  it should "get latest projects" in {
    val authHeader: Authorization = getAuthHeader

    Get("/project") ~> authHeader ~> projectRoutes.routes ~> check {
      responseAs[Seq[Project]] shouldEqual Seq.empty[Project]
    }
  }

  it should "create create a new advert with all fields" in {
    val authHeader: Authorization = getAuthHeader

    val advert1 = Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.now))
    val post: HttpRequest = Post(path, advert1)

    advertRoutes.routes ~> check {
      status === StatusCodes.Created
      responseAs[String] should not be empty
    }

    Get(path) ~> authHeader ~> advertRoutes.routes ~> check {
      val str = responseAs[String]
      val projects = responseAs[Seq[Project]]
      projects should not be empty
    }

  }

  def mockAdverts = Seq(
    Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.now)),
    Advert(2, "title2", DIESEL, 10, true, Some(40000), None),
    Advert(3, "title3", GASOLINE, 10, true, None, Some(LocalDate.now)),
    Advert(4, "title4", DIESEL, 10, true, None, None),
    Advert(1, "title1", GASOLINE, 10, true, Some(50000), Some(LocalDate.now)),
    Advert(5, "title5", DIESEL, 10, true, Some(60000), None),
    Advert(6, "title6", GASOLINE, 10, true, None, Some(LocalDate.now)),
    Advert(7, "title7", DIESEL, 10, true, None, None)
  )

}