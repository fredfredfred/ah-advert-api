package ah.advert.service

import ah.advert.entity.SimpleUser

import scala.concurrent.{ExecutionContext, Future}

/**
  * Service to resolve user credentials.
  * Created by ansgar on 2016-09-15.
  */
class UserService(implicit ec: ExecutionContext) {

  def authenticate(email: String, nonEncryptedPassword: String): Future[Option[SimpleUser]] = {
    Future(Some(SimpleUser(17, "Hans", email)))
  }

  def findByUsername(username: String)(implicit ec: ExecutionContext): Future[Option[SimpleUser]] = {
    Future(Some(SimpleUser(17, "Hans", "bla@bla.de")))
  }
}
