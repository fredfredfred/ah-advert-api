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

  it should "return an empty list on a fresh db" in {
    val authHeader: Authorization = getAuthHeader

    Get(testPath) ~> authHeader ~> advertRoutes.routes ~> check {
      responseAs[Seq[Advert]] shouldEqual Seq.empty[Advert]
    }
  }

  it should "create create a new advert" in {
    val advert1 = Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.now))

    Post(testPath, advert1) ~> advertRoutes.routes ~> check {
      status === StatusCodes.Created
      responseAs[String] should not be empty
    }

    Get(testPath)  ~> advertRoutes.routes ~> check {
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
    }
  }

  it should "update an existing advert" in {
    val date = LocalDate.now
    val advert1 = Advert(2, "title2", DIESEL, 20, false, Some(30000), Some(date))

    var id: String = ""
    Post(testPath, advert1) ~> advertRoutes.routes ~> check {
      status should === (StatusCodes.Created)
      id = responseAs[String]
      id should not be empty
    }

    var advert: Advert = null
    Get(s"$testPath/$id")  ~> advertRoutes.routes ~> check {
      status should === (StatusCodes.OK)
      val str = responseAs[String]
      str should not be (null)
      advert = responseAs[Advert]
      advert should not be (null)
      advert.id.toString should be(id)
      advert.title should be("title2")
      advert.fuel should be(DIESEL)
      advert.`new` should be(false)
      advert.mileage should be(Some(30000))
      advert.firstRegistration should be(Some(date))
    }


    val advert2= Advert(2, "title2", DIESEL, 20, true, Some(30000), Some(date)).copy(id = id.toLong)
    Put(s"$testPath/$id", advert2)  ~> advertRoutes.routes ~> check {
      status should === (StatusCodes.NoContent)
    }

    Get(s"$testPath/$id")  ~> advertRoutes.routes ~> check {
      status should === (StatusCodes.OK)
      val str = responseAs[String]
      str should not be (null)
      advert = responseAs[Advert]
      advert should not be (null)
      advert.`new` should be(true)
    }

  }
}