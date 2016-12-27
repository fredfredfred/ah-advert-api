package ah.advert.service.advert

import java.time.LocalDate

import ah.advert.BaseTestRoutes
import ah.advert.data.AdvertData
import ah.advert.entity.Advert
import ah.advert.entity.Fuel._
import ah.advert.json.JsonProtocol._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.ValidationRejection
import spray.json.DefaultJsonProtocol.jsonFormat7

class AdvertRoutesSpec extends BaseTestRoutes {

  val testPath = "/advert"

  it should "return an empty list on a fresh db" in {
    Get(testPath) ~> advertRoutes.routes ~> check {
      responseAs[Seq[Advert]] shouldEqual Seq.empty[Advert]
    }
  }

  it should "create create a new advert" in {
    val advert1 = Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.of(2010,10,10)))

    Post(testPath, advert1) ~> advertRoutes.routes ~> check {
      status === StatusCodes.Created
      responseAs[String] should not be empty
    }

    Get(testPath) ~> advertRoutes.routes ~> check {
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
    }
  }

  it should "update an existing advert" in {
    val date = LocalDate.of(2010,10,10)
    val advert1 = Advert(2, "title2", DIESEL, 20, false, Some(30000), Some(date))

    var id: String = ""
    Post(testPath, advert1) ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.Created)
      id = responseAs[String]
      id should not be empty
    }

    var advert: Advert = null
    Get(s"$testPath/$id") ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.OK)
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


    Put(s"$testPath/$id", advert.copy(`new` = true)) ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.NoContent)
    }

    Get(s"$testPath/$id") ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.OK)
      val str = responseAs[String]
      str should not be (null)
      advert = responseAs[Advert]
      advert should not be (null)
      advert.`new` should be(true)
    }

    Put(s"$testPath/$id", advert.copy(fuel = GASOLINE, mileage = None, firstRegistration = None)) ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.NoContent)
    }

    Get(s"$testPath/$id") ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.OK)
      val str = responseAs[String]
      str should not be null
      advert = responseAs[Advert]
      advert should not be null
      advert.fuel should be (GASOLINE)
      advert.mileage should be (None)
      advert.firstRegistration should be(None)
    }

  }

  it should "delete an advert" in {
    val date = LocalDate.now
    val advert1 = Advert(2, "deleteme", DIESEL, 40000, true, None, None)

    var id: String = ""
    Post(testPath, advert1) ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.Created)
      id = responseAs[String]
      id should not be empty
    }

    var advert: Advert = null
    Get(s"$testPath/$id") ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.OK)
      val str = responseAs[String]
      str should not be null
      advert = responseAs[Advert]
      advert.title should be("deleteme")
    }

    Delete(s"$testPath/$id") ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.NoContent)
    }

    Get(s"$testPath/$id") ~> advertRoutes.routes ~> check {
      status should ===(StatusCodes.NotFound)
    }
  }

  it should "return all adverts with default sorting by id" in {
    // create adverts
    AdvertData.mockAdvertsList1.foreach(advert =>
      Post(testPath, advert) ~> advertRoutes.routes ~> check {
        status should ===(StatusCodes.Created)
      }
    )

    Get(testPath) ~> advertRoutes.routes ~> check {
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
      (adverts, adverts.tail).zipped.forall(_.id <= _.id) should be(true)
    }
  }

  it should "return adverts sorted by price ascending" in {
    AdvertData.mockAdvertsList2.foreach(advert =>
      Post(testPath, advert) ~> advertRoutes.routes ~> check {
        status should ===(StatusCodes.Created)
      }
    )
    Get(s"$testPath?sort=price") ~> advertRoutes.routes ~> check {
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
      (adverts, adverts.tail).zipped.forall(_.price <= _.price) should be(true)
    }
  }

  it should "return adverts sorted by price descending" in {
    AdvertData.mockAdvertsList2.foreach(advert =>
      Post(testPath, advert) ~> advertRoutes.routes ~> check {
        status should ===(StatusCodes.Created)
      }
    )
    Get(s"$testPath?sort=price&order=desc") ~> advertRoutes.routes ~> check {
      val adverts = responseAs[Seq[Advert]]
      adverts should not be empty
      (adverts, adverts.tail).zipped.forall(_.price >= _.price) should be(true)
    }
  }

  it should "return reject an unknown sort field" in {
    Get(s"$testPath?sort=pretzel&order=desc") ~> advertRoutes.routes ~>  check {
      rejection shouldBe a [ValidationRejection]
    }
  }

  it should "return reject an unknown order field" in {
    Get(s"$testPath?sort=id&order=bla") ~> advertRoutes.routes ~>  check {
      rejection shouldBe a [ValidationRejection]
    }
  }

  it should "return reject a negative price" in {
    case class BrokenAdvert(
                       id: Long,
                       title: String,
                       fuel: Fuel,
                       price: Int,
                       `new`: Boolean,
                       mileage: Option[Int],
                       firstRegistration: Option[LocalDate])
    implicit val brokenAdvertFormat = jsonFormat7(BrokenAdvert.apply)
    val brokenAdvert = BrokenAdvert(1, "title1", GASOLINE, -10, true, Some(30000), Some(LocalDate.of(2010,10,10)))
    Post(testPath, brokenAdvert) ~> advertRoutes.routes ~> check {
      rejection shouldBe a [ValidationRejection]
    }
  }

}