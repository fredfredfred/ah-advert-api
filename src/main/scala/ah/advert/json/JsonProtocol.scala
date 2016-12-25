package ah.advert.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import ah.advert.entity.FuelEnum.FuelEnum
import ah.advert.entity.{Advert, FuelEnum}
import ah.advert.service.auth.{AuthResponse, LoginRequest}
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}
import spray.json.DefaultJsonProtocol._

object JsonProtocol {

  implicit val localDateJsonFormat = new RootJsonFormat[LocalDate] {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override def write(dateTime: LocalDate) = JsString(dateTime.format(formatter))

    override def read(json: JsValue): LocalDate = json match {
      case JsString(s) => LocalDate.parse(s, formatter)
      case _ => throw new DeserializationException(s"cannot parse json date-time value [${json.toString}]")
    }
  }

//  implicit val fuelEnumJsonFormat = new RootJsonFormat[FuelEnum#Value] {
//    override def write(fuel: FuelEnum) = JsString(fuel.toString)
//
//    override def read(json: JsValue): FuelEnum = json match {
//      case JsString(s) => FuelEnum.withName(json.toString)
//      case _ => throw new DeserializationException(s"cannot parse json for fuel enumeration [${json.toString}]")
//    }
//  }
//

  class EnumJsonConverter[T <: scala.Enumeration](enu: T) extends RootJsonFormat[T#Value] {
    def write(obj: T#Value) = JsString(obj.toString)
    def read(json: JsValue) = {
      json match {
        case JsString(txt) => enu.withName(txt)
        case something => throw new DeserializationException(s"cannot parse json for fuel enumeration [${json.toString}]")
      }
    }
  }

  implicit val fuelEnumJsonFormat = new EnumJsonConverter(FuelEnum)

  implicit val authResponseFormat = jsonFormat1(AuthResponse.apply)
  implicit val loginRequestFormat = jsonFormat3(LoginRequest.apply)
  implicit val advertFormat = jsonFormat7(Advert.apply)

}
