package ah.advert.module

import ah.advert.dao._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

trait DaoModule {

  import com.softwaremill.macwire._

  // injections
  lazy val advertDao: AdvertDao = wire[AdvertDaoImpl]

  // dependencies
  implicit val ec: ExecutionContext
  implicit val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("pgdb_prod")
  implicit val profile: JdbcProfile = dbConfig.driver

  // some initialization of the module
  def createSchemaTables = {
    if (!Await.result(advertDao.tableExists(), 5.seconds)) {
      advertDao.createTable()
    }
  }

  def ddlCreate = {
    advertDao.ddlCreate
  }

}