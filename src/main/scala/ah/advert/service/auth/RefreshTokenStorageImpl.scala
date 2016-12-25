package ah.advert.service.auth

import java.time.{Instant, OffsetDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.softwaremill.macwire._
import com.softwaremill.session.{RefreshTokenData, RefreshTokenLookupResult, RefreshTokenStorage}

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class RefreshTokenStorageImpl(implicit ec: ExecutionContext, actorSystem: ActorSystem)
  extends RefreshTokenStorage[SessionData] {

  lazy val dao = wire[RememberMeTokenDao]


  override def lookup(selector: String) = {
    dao.findBySelector(selector).map(_.map(t =>
      RefreshTokenLookupResult(t.tokenHash, t.time.toInstant.toEpochMilli, () => SessionData(t.userId))))
  }

  override def store(data: RefreshTokenData[SessionData]) =
    dao.add(RememberMeToken(17, data.selector, data.tokenHash, data.forSession.username,
      Instant.ofEpochMilli(data.expires).atOffset(ZoneOffset.UTC)))

  override def remove(selector: String) = dao.remove(selector)

  override def schedule[S](after: Duration)(op: => Future[S]) =
    actorSystem.scheduler.scheduleOnce(FiniteDuration(after.toSeconds, TimeUnit.SECONDS), new Runnable {
      override def run() = op
    })
}


class RememberMeTokenDao(implicit ec: ExecutionContext) {
  def remove(selector: String): Future[Unit] = Future {
    println("remove token");
    Success("")
  }

  def add(token: RememberMeToken): Future[Unit] = Future {
    println("add token");
    Success("")
  }

  def findBySelector(selector: String): Future[Option[RememberMeToken]] =
    Future(Some(
      RememberMeToken(17, "bla@bla.de", "HASHXYZ", "bla@bla.de",
        Instant.ofEpochMilli(0L).atOffset(ZoneOffset.UTC))))


}

case class RememberMeToken(id: Long, selector: String, tokenHash: String, userId: String, time: OffsetDateTime)