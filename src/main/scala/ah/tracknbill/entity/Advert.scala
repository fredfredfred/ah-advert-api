package ah.tracknbill.entity

import java.time.LocalDate

import ah.tracknbill.dao.common.CustomSlickMapper.Postgres._
import ah.tracknbill.entity.FuelEnum.FuelEnum
import ah.tracknbill.entity.common.{BaseEntity, BaseTable}
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

/**
  * Case class model and slick table for advert.
  * Created by ansgar on 2016-12-24.
  */
case class Advert(
                   id: Long,
                   title: String,
                   fuel: FuelEnum,
                   price: Int,
                   `new`: Boolean,
                   mileage: Option[Int],
                   firstRegistration: Option[LocalDate]) extends BaseEntity


class AdvertTable(tag: Tag) extends BaseTable[Advert](tag, "Advert") {
  def title = column[String]("name")

  def fuel = column[FuelEnum]("fuel")

  def price = column[Int]("price")

  def `new` = column[Boolean]("new")

  def mileage = column[Option[Int]]("mileage")

  def firstRegistration = column[Option[LocalDate]]("firstRegistration")

  def * = (
    id,
    title,
    fuel,
    price,
    `new`,
    mileage,
    firstRegistration
  ) <> (Advert.tupled, Advert.unapply)
}