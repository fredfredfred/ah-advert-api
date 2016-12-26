package ah.advert.service.advert

import ah.advert.BaseTest
import ah.advert.data.AdvertData
import ah.advert.entity.Advert

/**
  * Created by ansgar on 2016-12-26.
  */
class AdvertServiceSpec extends BaseTest {

  it should "sort by different sort fields" in {
    advertService.insert(AdvertData.mockAdvertsList2)
    var adverts: Seq[Advert] = waitForResult(advertService.findAll(AdvertSortField.id, SortOrder.asc))
    (adverts, adverts.tail).zipped.forall(_.id <= _.id) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.id, SortOrder.desc))
    (adverts, adverts.tail).zipped.forall(_.id >= _.id) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.title, SortOrder.asc))
    (adverts, adverts.tail).zipped.forall(_.title <= _.title) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.title, SortOrder.desc))
    (adverts, adverts.tail).zipped.forall(_.title >= _.title) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.fuel, SortOrder.asc))
    (adverts, adverts.tail).zipped.forall(_.fuel <= _.fuel) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.fuel, SortOrder.desc))
    (adverts, adverts.tail).zipped.forall(_.fuel >= _.fuel) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.price, SortOrder.asc))
    (adverts, adverts.tail).zipped.forall(_.price <= _.price) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.price, SortOrder.desc))
    (adverts, adverts.tail).zipped.forall(_.price >= _.price) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.`new`, SortOrder.asc))
    (adverts, adverts.tail).zipped.forall(_.`new` <= _.`new`) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.`new`, SortOrder.desc))
    (adverts, adverts.tail).zipped.forall(_.`new` >= _.`new`) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.mileage, SortOrder.asc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.mileage}, b=${b.mileage}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.mileage.map(ad => b.mileage.map(bd => ad < bd).getOrElse(true)).getOrElse(b.mileage.isEmpty)) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.mileage, SortOrder.desc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.mileage}, b=${b.mileage}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.mileage.map(ad => b.mileage.map(bd => ad > bd).getOrElse(true)).getOrElse(b.mileage.isEmpty)) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.firstRegistration, SortOrder.asc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.firstRegistration}, b=${b.firstRegistration}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isBefore bd).getOrElse(true)).getOrElse(b.firstRegistration.isEmpty)) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.firstRegistration, SortOrder.desc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.firstRegistration}, b=${b.firstRegistration}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isAfter bd).getOrElse(true)).getOrElse(b.firstRegistration.isEmpty)) should be(true)

  }

}
