package ah.advert.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import ah.advert.entity.{Advert, Fuel}
import ah.advert.service.advert.{AdvertSortField, SortOrder}
import ah.advert.service.auth.{AuthResponse, LoginRequest}
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

object JsonProtocol {

  implicit val localDateJsonFormat = new RootJsonFormat[LocalDate] {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override def write(dateTime: LocalDate) = JsString(dateTime.format(formatter))

    override def read(json: JsValue): LocalDate = json match {
      case JsString(s) => LocalDate.parse(s, formatter)
      case _ => throw new DeserializationException(s"cannot parse json date-time value [${json.toString}]")
    }
  }

  class EnumJsonConverter[T <: scala.Enumeration](enu: T) extends RootJsonFormat[T#Value] {
    def write(obj: T#Value) = JsString(obj.toString)

    def read(json: JsValue) = {
      json match {
        case JsString(txt) => enu.withName(txt)
        case something => throw new DeserializationException(s"cannot parse json for fuel enumeration [${json.toString}]")
      }
    }
  }

  implicit val fuelEnumJsonFormat = new EnumJsonConverter(Fuel)

  implicit val authResponseFormat = jsonFormat1(AuthResponse.apply)
  implicit val loginRequestFormat = jsonFormat3(LoginRequest.apply)
  implicit val advertFormat = jsonFormat7(Advert.apply)

}
