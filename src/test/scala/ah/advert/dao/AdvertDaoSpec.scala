package ah.advert.dao

import java.time.LocalDate

import ah.advert.BaseTest
import ah.advert.data.AdvertData
import ah.advert.entity.Fuel.{DIESEL, GASOLINE}
import ah.advert.entity.{Advert, Fuel}


class AdvertDaoSpec extends BaseTest {

  it should "create and find adverts" in {
    val count = waitForResult(advertDao.count)
    val idInserted: Long = waitForResult(advertDao.insert(Advert(1, "title1", Fuel.DIESEL, 10, true, Some(30000), Some(LocalDate.of(2012,12,12)))))
    idInserted should be(count + 1)

    val advert: Option[Advert] = waitForResult(advertDao.findById(idInserted))
    advert should not be empty
    advert.get.title should be("title1")

    val emptyAdvert: Option[Advert] = waitForResult(advertDao.findById(-1))
    emptyAdvert should be(empty)
  }

  it should "insert a seq of adverts" in {
    val mockAdverts = AdvertData.mockAdvertsList1
    val count = waitForResult(advertDao.count)
    val ids: Seq[Long] = waitForResult(advertDao.insert(mockAdverts))
    val newCount = waitForResult(advertDao.count)
    ids.size should be (mockAdverts.size)
    newCount should be (count +mockAdverts.size)
  }



}
