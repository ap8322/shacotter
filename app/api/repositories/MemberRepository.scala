package api.repositories

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import api.models.{Email, HashedPassword, Member}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import models.Tables

class MemberRepository @Inject()(
    val dbConfigProvider: DatabaseConfigProvider
)(
  implicit val ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  def findBy(memberId: Long): Future[Option[Member]] = {
    db.run(
     Tables.Member
        .filter(_.memberId === memberId)
        .result
        .headOption
         .map( _.map( r =>
           Member(
             memberId = r.memberId,
             name = r.name,
             password = HashedPassword(r.password)
           )
         )
       )
    )
  }

  def findBy(email: Email): Future[Option[Member]] = {
    db.run(
      Tables.Member
        .filter(_.email === email.value)
        .result
        .headOption
        .map( _.map( r =>
            Member(
              memberId = r.memberId,
              name = r.name,
              password = HashedPassword(r.password)
            )
          )
        )
    )
  }
}
