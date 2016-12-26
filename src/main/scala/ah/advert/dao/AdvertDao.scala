package ah.advert.dao

import ah.advert.dao.common.{BaseDao, BaseDaoImpl}
import ah.advert.entity.{Advert, AdvertTable}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext

/**
  * Access the database. Place to put advert specific queries.
  * Created by ansgar on 2016-12-24.
  */
trait AdvertDao extends BaseDao[AdvertTable, Advert] {

}

class AdvertDaoImpl(dbConfig: DatabaseConfig[JdbcProfile], profile: JdbcProfile)(implicit ec: ExecutionContext)
  extends BaseDaoImpl[AdvertTable, Advert](TableQuery[AdvertTable], dbConfig: DatabaseConfig[JdbcProfile], profile: JdbcProfile)
    with AdvertDao {

  val query: _root_.slick.driver.PostgresDriver.api.TableQuery[AdvertTable] = super.getQuery


}
