package ah.advert.dao.common

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime}

import ah.advert.entity.Fuel
import ah.advert.entity.Fuel.Fuel
import slick.ast.BaseTypedType
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.jdbc.JdbcType


object CustomSlickMapper {

  object Postgres {


    implicit val fuelEnumMapper = MappedColumnType.base[Fuel, String](
      e => e.toString,
      s => Fuel.withName(s)
    )

    implicit val localDateTimeColumnType: JdbcType[LocalDateTime] with BaseTypedType[LocalDateTime] =
      PostgresDriver.MappedColumnType.base[LocalDateTime, Timestamp](
        localDateTime => Timestamp.valueOf(localDateTime),
        timestamp => timestamp.toLocalDateTime
      )

    implicit val localDateColumnType = PostgresDriver.MappedColumnType.base[LocalDate, Date](
      localDate => Date.valueOf(localDate),
      date => date.toLocalDate
    )

  }

}