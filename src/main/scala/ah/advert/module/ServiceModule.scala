package ah.advert.module

import ah.advert.dao.AdvertDao
import ah.advert.service.advert.{AdvertService, AdvertServiceImpl}

import scala.concurrent.ExecutionContext

trait ServiceModule {
  implicit val ec: ExecutionContext

  import com.softwaremill.macwire._

  lazy val advertService: AdvertService = wire[AdvertServiceImpl]

  // dependencies
  def advertDao: AdvertDao

}
