package ah.advert.service.advert

import ah.advert.dao.AdvertDao
import ah.advert.entity.{Advert, FuelEnum}

import scala.concurrent.{ExecutionContext, Future}

trait AdvertService {
  def findAll(): Future[Seq[Advert]]

  def findById(id: Long): Future[Option[Advert]]

  def insert(advert: Advert): Future[Long]
}

class AdvertServiceImpl(advertDao: AdvertDao)(implicit ec: ExecutionContext) extends AdvertService {

  override def findAll(): Future[Seq[Advert]] = advertDao.findByFilter(x => true)

  override def findById(id: Long): Future[Option[Advert]] = advertDao.findById(id)

  override def insert(advert: Advert): Future[Long] = advertDao.insert(advert)
}
