package ah.advert.service.advert

/**
  * Created by ansgar on 2016-12-26.
  */
object AdvertSortField extends Enumeration {
  type AdvertSortField = Value
  val id, title, fuel, price, `new`, mileage, firstRegistration = Value
}
