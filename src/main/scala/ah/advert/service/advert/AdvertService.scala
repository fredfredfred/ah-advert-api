package ah.advert.service.advert

import ah.advert.dao.AdvertDao
import ah.advert.entity.Advert
import ah.advert.service.advert.AdvertSortField.AdvertSortField
import ah.advert.service.advert.Sorted.Sorted

import scala.concurrent.{ExecutionContext, Future}

trait AdvertService {
  def findAll(): Future[Seq[Advert]]

  def findAll(sortField: AdvertSortField, sorted: Sorted): Future[Seq[Advert]]

  def findById(id: Long): Future[Option[Advert]]

  def insert(advert: Advert): Future[Long]

  def insert(adverts: Seq[Advert]): Future[Seq[Long]]

  def update(advert: Advert): Future[Int]

  def deleteById(id: Long): Future[Int]

}

class AdvertServiceImpl(advertDao: AdvertDao)(implicit ec: ExecutionContext) extends AdvertService {

  // in a production service this should be limited or paged, which is skipped here for simplicity
  // all the sorting should be done in the DB as well and is skipped here for simplicity
  override def findAll(): Future[Seq[Advert]] = findAll(AdvertSortField.id, Sorted.asc)

  override def findAll(sortField: AdvertSortField, sorted: Sorted): Future[Seq[Advert]] = {
    val res: Future[Seq[Advert]] = advertDao.findByFilter(x => true)
    if (AdvertSortField.values.contains(sortField)) {
      res.map(seq => seq.sortWith(AdvertSortFunction.sortingFunc(sortField, sorted)))
    } else {
      res // silently ignore unknown sort fields
    }
  }

  override def findById(id: Long): Future[Option[Advert]] = advertDao.findById(id)

  override def insert(advert: Advert): Future[Long] = advertDao.insert(advert)

  override def insert(adverts: Seq[Advert]): Future[Seq[Long]] = advertDao.insert(adverts)

  override def update(advert: Advert): Future[Int] = advertDao.update(advert)

  override def deleteById(id: Long): Future[Int] = advertDao.deleteById(id)


}
