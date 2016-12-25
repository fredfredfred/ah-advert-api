package ah.advert.dao.common

import ah.advert.BaseTest
import ah.advert.entity.common.{BaseEntity, BaseTable}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag
import slick.profile.SqlStreamingAction

import scala.concurrent.Future

/**
  * Tests the BaseDao. Is no a base class for testing itself.
  */
class BaseDaoSpec extends BaseTest {

  val db = dbConfig.db

  override def afterAll: Unit = {
    db.close()
  }

  it should "createTable" in {
    case class TestEntity(id: Long, name: String) extends BaseEntity
    val tableName = "TestEntity"

    class TestEntityTable(tag: Tag) extends BaseTable[TestEntity](tag, "TestEntity") {
      def name = column[String]("name", O.Length(64))

      def * = (id, name) <> (TestEntity.tupled, TestEntity.unapply)
    }

    val testEntityDao: BaseDaoImpl[TestEntityTable, TestEntity] = new BaseDaoImpl[TestEntityTable, TestEntity](TableQuery[TestEntityTable], dbConfig: DatabaseConfig[JdbcProfile], profile: JdbcProfile) {}

    if (listTables.contains(tableName)) {
      waitForResult(testEntityDao.dropTable())
    }
    waitForResult(testEntityDao.createTable)
    if (listTables.contains(tableName)) {
      waitForResult(testEntityDao.dropTable())
    }
  }

  it should "execute plain SQL" in {
    val tableName = "coffees"

    if (listTables.contains(tableName)) {
      waitForResult(db.run(dropCoffees))
    }
    waitForResult(db.run(createCoffees))
    if (listTables.contains(tableName)) {
      waitForResult(db.run(dropCoffees))
    }

    def createCoffees: DBIO[Int] =
      sqlu"""CREATE TABLE coffees (
    name VARCHAR NOT NULL,
    sup_id INT NOT NULL,
    price FLOAT NOT NULL,
    sales INT NOT NULL,
    total INT NOT NULL
    )
    """

    def dropCoffees: DBIO[Int] =
      sqlu"DROP TABLE coffees"

  }

  def listTables = {
    def listTables: SqlStreamingAction[Vector[String], String, Effect] =
      sql"""SELECT table_name FROM information_schema.tables WHERE table_schema='public'""".as[String]

    val futureList: Future[Vector[String]] = db.run(listTables)
    val result: Vector[String] = waitForResult(futureList)
    result
  }

}
