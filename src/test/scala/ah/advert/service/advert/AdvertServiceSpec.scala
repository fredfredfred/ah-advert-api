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
    var adverts: Seq[Advert] = waitForResult(advertService.findAll(AdvertSortField.id, Sorted.asc))
    (adverts, adverts.tail).zipped.forall(_.id <= _.id) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.id, Sorted.desc))
    (adverts, adverts.tail).zipped.forall(_.id >= _.id) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.title, Sorted.asc))
    (adverts, adverts.tail).zipped.forall(_.title <= _.title) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.title, Sorted.desc))
    (adverts, adverts.tail).zipped.forall(_.title >= _.title) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.fuel, Sorted.asc))
    (adverts, adverts.tail).zipped.forall(_.fuel <= _.fuel) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.fuel, Sorted.desc))
    (adverts, adverts.tail).zipped.forall(_.fuel >= _.fuel) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.price, Sorted.asc))
    (adverts, adverts.tail).zipped.forall(_.price <= _.price) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.price, Sorted.desc))
    (adverts, adverts.tail).zipped.forall(_.price >= _.price) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.`new`, Sorted.asc))
    (adverts, adverts.tail).zipped.forall(_.`new` <= _.`new`) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.`new`, Sorted.desc))
    (adverts, adverts.tail).zipped.forall(_.`new` >= _.`new`) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.mileage, Sorted.asc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.mileage}, b=${b.mileage}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.mileage.map(ad => b.mileage.map(bd => ad < bd).getOrElse(true)).getOrElse(b.mileage.isEmpty)) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.mileage, Sorted.desc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.mileage}, b=${b.mileage}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.mileage.map(ad => b.mileage.map(bd => ad > bd).getOrElse(true)).getOrElse(b.mileage.isEmpty)) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.firstRegistration, Sorted.asc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.firstRegistration}, b=${b.firstRegistration}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isBefore bd).getOrElse(true)).getOrElse(b.firstRegistration.isEmpty)) should be(true)

    adverts = waitForResult(advertService.findAll(AdvertSortField.firstRegistration, Sorted.desc))
    (adverts, adverts.tail).zipped.foreach((a, b) => println(s"a=${a.firstRegistration}, b=${b.firstRegistration}"))
    (adverts, adverts.tail).zipped.forall((a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isAfter bd).getOrElse(true)).getOrElse(b.firstRegistration.isEmpty)) should be(true)

  }

}
