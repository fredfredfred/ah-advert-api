package ah.advert.service.advert

import ah.advert.entity.Advert
import ah.advert.service.advert.AdvertSortField.AdvertSortField
import ah.advert.service.advert.Sorted.Sorted

/**
  * Created by ansgar on 2016-12-26.
  */
object AdvertSortFunction {
  // Defines a sorting function for the pair (field, order)
  def sortingFunc(fieldsWithOrder: (AdvertSortField, Sorted)): (Advert, Advert) => Boolean = fieldsWithOrder match {
    case (AdvertSortField.id, Sorted.asc) => _.id < _.id
    case (AdvertSortField.id, Sorted.desc) => _.id > _.id
    case (AdvertSortField.title, Sorted.asc) => _.title < _.title
    case (AdvertSortField.title, Sorted.desc) => _.title > _.title
    case (AdvertSortField.fuel, Sorted.asc) => _.fuel.toString < _.fuel.toString
    case (AdvertSortField.fuel, Sorted.desc) => _.fuel.toString > _.fuel.toString
    case (AdvertSortField.price, Sorted.asc) => _.price < _.price
    case (AdvertSortField.price, Sorted.desc) => _.price > _.price
    case (AdvertSortField.`new`, Sorted.asc) => _.`new` < _.`new`
    case (AdvertSortField.`new`, Sorted.desc) => _.`new` > _.`new`
    case (AdvertSortField.mileage, Sorted.asc) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad < bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.mileage, Sorted.desc) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad > bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.firstRegistration, Sorted.asc) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isBefore bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.firstRegistration, Sorted.desc) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isAfter bd).getOrElse(true)).getOrElse(false)
    case _ => (_, _) => false
  }


}
