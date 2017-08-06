package api.repositories

import javax.inject.Inject

import scala.concurrent.Future

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import models.Tables

class MemberRepository @Inject()(
    val dbConfigProvider: DatabaseConfigProvider,
    val tables: Tables
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._
  import tables.{Member, MemberRow}

  def findBy(memberId: Long): Future[Option[MemberRow]] = {
    db.run(
      Member
        .filter(_.memberId === memberId)
        .result
        .headOption
    )
  }

  def findBy(email: String): Future[Option[MemberRow]] = {
    db.run(
      Member
        .filter(_.email === email)
        .result
        .headOption
    )
  }
}
