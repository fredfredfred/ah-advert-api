package ah.advert.dao

import java.time.LocalDate

import ah.advert.BaseTest
import ah.advert.entity.Fuel.{DIESEL, GASOLINE}
import ah.advert.entity.{Advert, Fuel}


class AdvertDaoSpec extends BaseTest {

  it should "create and find adverts" in {
    val count = waitForResult(advertDao.count)
    val idInserted: Long = waitForResult(advertDao.insert(Advert(1, "title1", Fuel.DIESEL, 10, true, Some(30000), Some(LocalDate.now))))
    idInserted should be(count + 1)

    val advert: Option[Advert] = waitForResult(advertDao.findById(idInserted))
    advert should not be empty
    advert.get.title should be("title1")

    val emptyAdvert: Option[Advert] = waitForResult(advertDao.findById(-1))
    emptyAdvert should be(empty)
  }

  it should "insert a seq of adverts" in {
    val count = waitForResult(advertDao.count)
    val ids: Seq[Long] = waitForResult(advertDao.insert(mockAdverts))
    val newCount = waitForResult(advertDao.count)
    ids.size should be (mockAdverts.size)
    newCount should be (count +mockAdverts.size)
  }

  def mockAdverts = Seq(
    Advert(1, "title1", GASOLINE, 10, `new` = true, Some(30000), Some(LocalDate.now)),
    Advert(2, "title2", DIESEL, 10, `new` = true, Some(40000), None),
    Advert(3, "title3", GASOLINE, 10, `new` = true, None, Some(LocalDate.now)),
    Advert(4, "title4", DIESEL, 10, `new` = true, None, None),
    Advert(1, "title1", GASOLINE, 10, `new` = true, Some(50000), Some(LocalDate.now)),
    Advert(5, "title5", DIESEL, 10, `new` = true, Some(60000), None),
    Advert(6, "title6", GASOLINE, 10, `new` = true, None, Some(LocalDate.now)),
    Advert(7, "title7", DIESEL, 10, `new` = true, None, None)
  )


}
