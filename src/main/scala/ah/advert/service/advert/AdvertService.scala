package ah.advert.service.advert

import ah.advert.dao.AdvertDao
import ah.advert.entity.{Advert, Fuel}

import scala.concurrent.{ExecutionContext, Future}

trait AdvertService {
  def findAll(): Future[Seq[Advert]]

  def findById(id: Long): Future[Option[Advert]]

  def insert(advert: Advert): Future[Long]

  def update(advert: Advert): Future[Int]

  def deleteById(id: Long): Future[Int]

}

class AdvertServiceImpl(advertDao: AdvertDao)(implicit ec: ExecutionContext) extends AdvertService {

  override def findAll(): Future[Seq[Advert]] = advertDao.findByFilter(x => true) // this should be limited, paged

  override def findById(id: Long): Future[Option[Advert]] = advertDao.findById(id)

  override def insert(advert: Advert): Future[Long] = advertDao.insert(advert)

  override def update(advert: Advert): Future[Int] = advertDao.update(advert)

  override def deleteById(id: Long): Future[Int] = advertDao.deleteById(id)
}
