package ah.tracknbill.entity.common

import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
}