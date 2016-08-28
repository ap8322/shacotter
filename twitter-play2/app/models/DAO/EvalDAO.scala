package models.dao

import javax.inject.Inject

import models.Tables.{Eval, EvalRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/18.
  */
class EvalDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def insert(tweetId: Int, evalStatus: Int, memberId: Int): Future[Int] = {
    db.run(Eval += EvalRow(tweetId, evalStatus, memberId))
  }

  def update(tweetId: Int, evalStatus: Int, memberId: Int): Future[Int] = {
    db.run(Eval.filter(e =>
      e.tweetId === tweetId.bind && e.memberId === memberId.bind
    ).update(
      EvalRow(tweetId, evalStatus, memberId))
    )
  }

  def delete(tweetId: Int, memberId: Int): Future[Int] = {
    db.run(Eval.filter(e =>
      e.tweetId === tweetId.bind && e.memberId === memberId
    ).delete)
  }
}