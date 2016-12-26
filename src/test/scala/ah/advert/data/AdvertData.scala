package ah.advert.data

import java.time.LocalDate

import ah.advert.entity.Advert
import ah.advert.entity.Fuel.{DIESEL, GASOLINE}

/**
  * Created by ansgar on 2016-12-26.
  */
object AdvertData {
  val mockAdvertsList1 = Seq(
    Advert(1, "title1", GASOLINE, 10, `new` = true, Some(30000), Some(LocalDate.now)),
    Advert(1, "title2", DIESEL, 10, `new` = true, Some(40000), None),
    Advert(1, "title3", GASOLINE, 10, `new` = true, None, Some(LocalDate.now)),
    Advert(1, "title4", DIESEL, 10, `new` = true, None, None),
    Advert(1, "title1", GASOLINE, 10, `new` = true, Some(50000), Some(LocalDate.now)),
    Advert(1, "title5", DIESEL, 10, `new` = true, Some(60000), None),
    Advert(1, "title6", GASOLINE, 10, `new` = true, None, Some(LocalDate.now)),
    Advert(1, "title7", DIESEL, 10, `new` = true, None, None)
  )

  val mockAdvertsList2 = Seq(
    Advert(17, "A", GASOLINE, 1, `new` = true, Some(30000), Some(LocalDate.now.plusDays(4))),
    Advert(16, "Z", DIESEL, 7, `new` = false, Some(40000), None),
    Advert(15, "D", GASOLINE, 4, `new` = true, None, Some(LocalDate.now.plusDays(1))),
    Advert(13, "A", DIESEL, 10, `new` = false, None, None),
    Advert(11, "BMW", GASOLINE, 3, `new` = true, Some(50000), Some(LocalDate.now.plusDays(3))),
    Advert(1, "Audi", DIESEL, 2, `new` = false, Some(60000), None),
    Advert(2, "Mercedes", GASOLINE, 9, `new` = true, None, Some(LocalDate.now)),
    Advert(5, "F", DIESEL, 2, `new` = true, None, None)
  )

}
