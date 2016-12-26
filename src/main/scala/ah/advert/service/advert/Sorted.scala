package ah.advert.service.advert

/**
  * Created by ansgar on 2016-12-26.
  */
sealed trait Sorted

object Sorted {

  case object ASC extends Sorted

  case object DESC extends Sorted

}
