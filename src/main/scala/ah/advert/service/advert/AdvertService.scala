package ah.advert.service.advert

import ah.advert.dao.AdvertDao
import ah.advert.entity.Advert
import ah.advert.service.advert.Sorted._

import scala.concurrent.{ExecutionContext, Future}

trait AdvertService {
  def findAll(): Future[Seq[Advert]]

  def findAll(sortField: String, sorted: Sorted): Future[Seq[Advert]]

  def findById(id: Long): Future[Option[Advert]]

  def insert(advert: Advert): Future[Long]

  def update(advert: Advert): Future[Int]

  def deleteById(id: Long): Future[Int]

}

class AdvertServiceImpl(advertDao: AdvertDao)(implicit ec: ExecutionContext) extends AdvertService {

  // in a production service this should be limited or paged, which is skipped here for simplicity
  // all the sorting should be done in the DB as well and is skipped here for simplicity
  override def findAll(): Future[Seq[Advert]] = findAll("id", ASC)

  override def findAll(sortField: String, sorted: Sorted): Future[Seq[Advert]] = {
    val res: Future[Seq[Advert]] = advertDao.findByFilter(x => true)
    res.map(seq => seq.sortWith(sortingFunc(sortField, ASC)))
  }

  override def findById(id: Long): Future[Option[Advert]] = advertDao.findById(id)

  override def insert(advert: Advert): Future[Long] = advertDao.insert(advert)

  override def update(advert: Advert): Future[Int] = advertDao.update(advert)

  override def deleteById(id: Long): Future[Int] = advertDao.deleteById(id)

  // List with all the available sorting fields.
  val sortingFields = Seq("id", "title", "fuel", "price", "mileage", "firstRegistration")

  // Defines a sorting function for the pair (field, order)
  def sortingFunc(fieldsWithOrder: (String, Sorted)): (Advert, Advert) => Boolean = fieldsWithOrder match {
    case ("id", ASC) => _.id < _.id
    case ("id", DESC) => _.id > _.id
    case ("title", ASC) => _.title < _.title
    case ("title", DESC) => _.title > _.title
    case ("fuel", ASC) => _.fuel.toString < _.fuel.toString
    case ("fuel", DESC) => _.fuel.toString > _.fuel.toString
    case ("price", ASC) => _.price > _.price
    case ("price", DESC) => _.price > _.price
    case ("mileage", ASC) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad < bd).getOrElse(true)).getOrElse(false)
    case ("mileage", DESC) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad > bd).getOrElse(true)).getOrElse(false)
    case ("firstRegistration", ASC) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isBefore bd).getOrElse(true)).getOrElse(false)
    case ("firstRegistration", DESC) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isAfter bd).getOrElse(true)).getOrElse(false)
    case _ => (_, _) => false
  }

}
