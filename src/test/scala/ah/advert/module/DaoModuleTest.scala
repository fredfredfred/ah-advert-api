package ah.advert.module

import ah.advert.dao._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

trait DaoModuleTest {

  import com.softwaremill.macwire._

  implicit val ec: ExecutionContext

  implicit val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("pgdb_test")
  implicit val profile: JdbcProfile = dbConfig.driver

  lazy val advertDao: AdvertDao = wire[AdvertDaoImpl]

  // some initialization of the module
  def createSchemaTables = {
    if (!Await.result(advertDao.tableExists(), 5.seconds)) {
      advertDao.createTable()
    }
  }


}