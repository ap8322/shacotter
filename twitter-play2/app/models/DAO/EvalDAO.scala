package models.dao

import javax.inject.Inject

import models.Tables.{Tweetevaluete, TweetevalueteRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import utils.SystemClock

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/18.
  */
class EvalDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with SystemClock {

  def insert(tweetId: Long, evalStatus: Int, memberId: Long): Future[Int] = {
    db.run(Tweetevaluete += TweetevalueteRow(
      tweetId,
      evalStatus,
      memberId,
      currentTimestamp,
      memberId.toString,
      currentTimestamp,
      memberId.toString,
      1.toLong)
    )
  }

  def update(tweetId: Long, evalStatus: Int, memberId: Long): Future[Int] = {
    db.run(Tweetevaluete.filter(e =>
      e.tweetId === tweetId.bind && e.memberId === memberId.bind
    ).map(e =>
      (e.evalueteStatus, e.updateDatetime, e.updateUser)
    ).update(
      (
        evalStatus,
        currentTimestamp,
        memberId.toString)
    )
    )
  }

  def delete(tweetId: Long, memberId: Long): Future[Int] = {
    db.run(Tweetevaluete.filter(e =>
      e.tweetId === tweetId.bind && e.memberId === memberId
    ).delete)
  }
}