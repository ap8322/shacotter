package models.dao

import javax.inject.Inject

import models.Tables.{TweetEvaluate, TweetEvaluateRow}
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

  def insert(tweetId: Long, evaluateStatus: String, memberId: Long): Future[Int] = {
    db.run(TweetEvaluate += TweetEvaluateRow(
      tweetId,
      evaluateStatus,
      memberId,
      currentTimestamp,
      memberId.toString,
      currentTimestamp,
      memberId.toString,
      1.toLong)
    )
  }

  def update(tweetId: Long, evaluateStatus: String, memberId: Long): Future[Int] = {
    db.run(TweetEvaluate.filter(e =>
      e.tweetId === tweetId.bind && e.memberId === memberId.bind
    ).map(e =>
      (e.evaluateStatus, e.updateDatetime, e.updateUser)
    ).update(
      (
        evaluateStatus,
        currentTimestamp,
        memberId.toString)
    )
    )
  }

  def delete(tweetId: Long, memberId: Long): Future[Int] = {
    db.run(TweetEvaluate.filter(e =>
      e.tweetId === tweetId.bind && e.memberId === memberId
    ).delete)
  }
}