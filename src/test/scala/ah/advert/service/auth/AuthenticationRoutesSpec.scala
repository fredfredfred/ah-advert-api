package ah.advert.service.auth

import ah.advert.BaseTestRoutes
import ah.advert.json.JsonProtocol._

/**
  * Created by ansgar on 2016-11-23.
  */
class AuthenticationRoutesSpec extends BaseTestRoutes {

  it should "get an authentication token" in {
    Post("/auth", LoginRequest("email", "password", Some(false))) ~> authenticationRoutes.routes ~> check {
      val resp: AuthResponse = responseAs[AuthResponse]
      resp should not be null
    }
  }

}
