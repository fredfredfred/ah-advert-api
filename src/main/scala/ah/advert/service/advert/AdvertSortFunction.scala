package ah.advert.service.advert

import ah.advert.entity.Advert
import ah.advert.service.advert.AdvertSortField.AdvertSortField
import ah.advert.service.advert.Sorted._

/**
  * Created by ansgar on 2016-12-26.
  */
object AdvertSortFunction {
  // Defines a sorting function for the pair (field, order)
  def sortingFunc(fieldsWithOrder: (AdvertSortField, Sorted)): (Advert, Advert) => Boolean = fieldsWithOrder match {
    case (AdvertSortField.id, ASC) => _.id < _.id
    case (AdvertSortField.id, DESC) => _.id > _.id
    case (AdvertSortField.title, ASC) => _.title < _.title
    case (AdvertSortField.title, DESC) => _.title > _.title
    case (AdvertSortField.fuel, ASC) => _.fuel.toString < _.fuel.toString
    case (AdvertSortField.fuel, DESC) => _.fuel.toString > _.fuel.toString
    case (AdvertSortField.price, ASC) => _.price < _.price
    case (AdvertSortField.price, DESC) => _.price > _.price
    case (AdvertSortField.`new`, ASC) => _.`new` < _.`new`
    case (AdvertSortField.`new`, DESC) => _.`new` > _.`new`
    case (AdvertSortField.mileage, ASC) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad < bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.mileage, DESC) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad > bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.firstRegistration, ASC) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isBefore bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.firstRegistration, DESC) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isAfter bd).getOrElse(true)).getOrElse(false)
    case _ => (_, _) => false
  }


}
