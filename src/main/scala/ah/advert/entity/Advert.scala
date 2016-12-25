package ah.advert.entity

import java.time.LocalDate

import ah.advert.dao.common.CustomSlickMapper.Postgres._
import ah.advert.entity.Fuel.Fuel
import ah.advert.entity.common.{BaseEntity, BaseTable}
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

/**
  * Case class model and slick table for advert.
  * Created by ansgar on 2016-12-24.
  */
case class Advert(
                   id: Long,
                   title: String,
                   fuel: Fuel,
                   price: Int,
                   `new`: Boolean,
                   mileage: Option[Int],
                   firstRegistration: Option[LocalDate]) extends BaseEntity


class AdvertTable(tag: Tag) extends BaseTable[Advert](tag, "Advert") {
  def title = column[String]("title")

  def fuel = column[Fuel]("fuel")

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