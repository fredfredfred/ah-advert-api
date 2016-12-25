package ah.advert.dao.common

import ah.advert.entity.common.{BaseEntity, BaseTable}
import slick.backend.DatabaseConfig
import slick.dbio.Effect.Read
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.jdbc.meta.MTable
import slick.lifted.CanBeQueryCondition
import slick.profile.FixedSqlStreamingAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait BaseDao[T, A] {
  def count(): Future[Int]

  def insert(row: A): Future[Long]

  def insert(rows: Seq[A]): Future[Seq[Long]]

  def update(row: A): Future[Int]

  def update(rows: Seq[A]): Future[Unit]

  def findById(id: Long): Future[Option[A]]

  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]]

  def deleteById(id: Long): Future[Int]

  def deleteById(ids: Seq[Long]): Future[Int]

  def deleteByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Int]

  def createTable(): Future[Unit]

  def dropTable(): Future[Unit]

  def tableExists(): Future[Boolean]

  def ddlCreate: Iterator[String]

  def ddlDrop: Iterator[String]
}

class BaseDaoImpl[T <: BaseTable[A], A <: BaseEntity]
(query: TableQuery[T], dbConfig: DatabaseConfig[JdbcProfile], profile: JdbcProfile)
  extends BaseDao[T, A] {

  val db = dbConfig.db

  import profile.api._

  protected def getQuery = this.query

  override def count(): Future[Int] = {
    db.run(query.length.result)
  }

  override def insert(row: A): Future[Long] = {
    insert(Seq(row)).map(_.head)
  }

  override def insert(rows: Seq[A]): Future[Seq[Long]] = {
    db.run(query returning query.map(_.id) ++= rows)
  }

  override def update(row: A): Future[Int] = {
    db.run(query.filter(_.id === row.id).update(row))
  }

  override def update(rows: Seq[A]): Future[Unit] = {
    db.run(DBIO.seq(rows.map(r => query.filter(_.id === r.id).update(r)): _*))
  }

  override def findById(id: Long): Future[Option[A]] = {
    val bla: FixedSqlStreamingAction[Seq[A], A, Read] = query.filter(_.id === id).result
    db.run(query.filter(_.id === id).result.headOption)
  }

  override def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    db.run(query.withFilter(f).result)
  }

  override def deleteById(id: Long): Future[Int] = {
    deleteById(Seq(id))
  }

  override def deleteById(ids: Seq[Long]): Future[Int] = {
    db.run(query.filter(_.id.inSet(ids)).delete)
  }

  override def deleteByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Int] = {
    db.run(query.withFilter(f).delete)
  }

  override def createTable(): Future[Unit] = {
    db.run(DBIO.seq(query.schema.create))
  }

  override def dropTable(): Future[Unit] = {
    db.run(DBIO.seq(query.schema.drop))
  }

  override def tableExists(): Future[Boolean] = {
    val tableName = query.baseTableRow.tableName
    val tablesFutures: Future[Vector[MTable]] = db.run(MTable.getTables(tableName))
    tablesFutures.map(vector => vector.exists(mTable => mTable.name.name == tableName))
  }

  override def ddlCreate: Iterator[String] = {
    query.schema.createStatements
  }

  override def ddlDrop: Iterator[String] = {
    query.schema.dropStatements
  }
}