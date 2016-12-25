package ah.advert.json

import java.time.LocalDate

import ah.advert.entity.Fuel.GASOLINE
import ah.advert.entity.{Advert, Fuel}
import ah.advert.json.JsonProtocol._
import org.scalatest.{FlatSpec, Matchers}
import spray.json._

/**
  * Created by ansgar on 2016-12-25.
  */
class JsonProtocolSpec extends FlatSpec with Matchers {

  it should "serialize adverts to json" in {
    val advert = Advert(1, "title1", GASOLINE, 10, true, Some(30000), Some(LocalDate.now))
    val result = advertFormat.write(advert)
    val jsonString = """{"mileage":30000,"price":10,"fuel":"GASOLINE","id":1,"new":true,"firstRegistration":"2016-12-25","title":"title1"}"""
    result.toString should be(jsonString)

  }

  it should "deserialize to adverts" in {
    val jsonString = """{"id":1,"mileage":30000,"price":10,"fuel":"GASOLINE","new":true,"firstRegistration":"2016-12-25","title":"title1"}"""
    val jsonAst = jsonString.parseJson
    val advert = advertFormat.read(jsonAst)
    advert.id should be(1L)
    advert.title should be("title1")
    advert.fuel should be(Fuel.GASOLINE)
    advert.`new` should be(true)
    advert.price should be(10)
    advert.mileage should be(Some(30000))
  }

  it should "serialize FuelEnum" in {
    val res = fuelEnumJsonFormat.write(Fuel.DIESEL)
    res.toString should be("\"DIESEL\"")
  }

  it should "deserialize FuelEnum" in {
    val json = "\"DIESEL\""
    val jsonAst = json.parseJson
    val res = fuelEnumJsonFormat.read(jsonAst)
    res should be(Fuel.DIESEL)
  }

}
