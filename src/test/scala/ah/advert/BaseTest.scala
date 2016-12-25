package ah.advert

import java.time.LocalDate

import ah.advert.module.TestModules
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable}

trait BaseTest extends FlatSpec with TestModules with Matchers
  with BeforeAndAfterAll with BeforeAndAfterEach with MockFactory {

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)

  def waitForResult[T](awaitable: Awaitable[T]): T = Await.result(awaitable, 5.seconds)

  override def beforeAll = {
    createTables()
  }

  override def afterAll: Unit = {
    dropTables()
    dbConfig.db.close()
  }

  def createTables() {
    if (!waitForResult(advertDao.tableExists())) {
      waitForResult(advertDao.createTable())
    }
  }

  def dropTables() = {
    if (waitForResult(advertDao.tableExists())) {
      waitForResult(advertDao.dropTable())
    }
  }

}
