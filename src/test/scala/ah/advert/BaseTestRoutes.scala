package ah.advert

import ah.advert.service.auth.{AuthResponse, LoginRequest}
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import ah.advert.json.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait BaseTestRoutes extends BaseTest with ScalatestRouteTest with DefaultJsonProtocol with SprayJsonSupport{

  override def beforeAll = {
    createTables()
  }

  override def afterAll: Unit = {
    dropTables()
    dbConfig.db.close()
  }

  def getAuthHeader: Authorization = {
    var token: String = ""
    Post("/auth", LoginRequest("email", "password", Some(false))) ~> authenticationRoutes.routes ~> check {
      val resp = responseAs[AuthResponse]
      token = resp.message
    }
    val authHeader: Authorization = Authorization(OAuth2BearerToken(token))
    authHeader
  }

}
