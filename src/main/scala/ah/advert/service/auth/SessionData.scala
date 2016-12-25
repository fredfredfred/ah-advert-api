package ah.advert.service.auth

import com.softwaremill.session.{JValueSessionSerializer, SessionSerializer}
import org.json4s._

case class SessionData(username: String)

object SessionData {
  implicit def serializer: SessionSerializer[SessionData, JValue] = JValueSessionSerializer.caseClass[SessionData]
}
