package ah.advert.service.advert

import ah.advert.entity.Advert
import ah.advert.service.advert.AdvertSortField.AdvertSortField
import ah.advert.service.advert.SortOrder.SortOrder

/**
  * Created by ansgar on 2016-12-26.
  */
object AdvertSortFunction {
  // Defines a sorting function for the pair (field, order)
  def sortingFunc(fieldsWithOrder: (AdvertSortField, SortOrder)): (Advert, Advert) => Boolean = fieldsWithOrder match {
    case (AdvertSortField.id, SortOrder.asc) => _.id < _.id
    case (AdvertSortField.id, SortOrder.desc) => _.id > _.id
    case (AdvertSortField.title, SortOrder.asc) => _.title < _.title
    case (AdvertSortField.title, SortOrder.desc) => _.title > _.title
    case (AdvertSortField.fuel, SortOrder.asc) => _.fuel.toString < _.fuel.toString
    case (AdvertSortField.fuel, SortOrder.desc) => _.fuel.toString > _.fuel.toString
    case (AdvertSortField.price, SortOrder.asc) => _.price < _.price
    case (AdvertSortField.price, SortOrder.desc) => _.price > _.price
    case (AdvertSortField.`new`, SortOrder.asc) => _.`new` < _.`new`
    case (AdvertSortField.`new`, SortOrder.desc) => _.`new` > _.`new`
    case (AdvertSortField.mileage, SortOrder.asc) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad < bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.mileage, SortOrder.desc) => (a, b) => a.mileage.map(ad => b.mileage.map(bd => ad > bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.firstRegistration, SortOrder.asc) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isBefore bd).getOrElse(true)).getOrElse(false)
    case (AdvertSortField.firstRegistration, SortOrder.desc) => (a, b) => a.firstRegistration.map(ad => b.firstRegistration.map(bd => ad isAfter bd).getOrElse(true)).getOrElse(false)
    case _ => (_, _) => false
  }


}
