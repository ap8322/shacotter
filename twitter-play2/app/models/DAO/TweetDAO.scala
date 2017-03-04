package models.dao

import javax.inject.Inject

import models.Forms.TweetInfo
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import utils.EvaluateStatesCode._
import utils.SystemClock

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/03.
  *
  */

// done ベタ書きSQLから卒業する｡
// done slick で書きなおした部分でいいね､どうでもいいねどちらも0のものだけ､検索されない｡
// todo 共通部分が多いので冗長部分をなくす｡
// todo ページング機能実装

class TweetDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with SystemClock {

  /**
    * ツイートを追加
    *
    * @param myId    Login Member Id
    * @param myTweet Other Member Id
    * @return
    */
  def add(myId: Long, myTweet: String): Future[Int] = {
    db.run(
      Tweet.map(t =>
        (
          t.memberId,
          t.tweet,
          t.tweetAt,
          t.registerDatetime,
          t.registerUser,
          t.updateDatetime,
          t.updateUser,
          t.versionNo)
      ) += (
        myId,
        myTweet,
        currentTimestamp,
        currentTimestamp,
        myId.toString,
        currentTimestamp,
        myId.toString,
        1.toLong)
    )
  }

  /**
    * 自分のツイートを取得
    *
    * @param id Login Member Id
    * @return
    */
  def selectMyTweet(id: Long): Future[Seq[TweetInfo]] = {
    //  SELECT m.name,
    //    t.tweet_id,
    //    t.tweet,
    //    (select COUNT(*) FROM Eval se1 WHERE eval_status = 1 AND se1.tweet_id = t.tweet_id),
    //    (select COUNT(*) FROM Eval se2 WHERE eval_status = 0 AND se2.tweet_id = t.tweet_id),
    //    (select eval_status from Eval where member_id = $id and tweet_id = t.tweet_id)
    //  FROM Member m
    //    JOIN Tweet t ON m.member_id = t.member_id
    //    LEFT JOIN Eval e ON t.tweet_id = e.tweet_id
    //  WHERE m.member_id = $id

    val page = 0

    val dbio = Member
      .join(Tweet)
      .on(_.memberId === _.memberId)
      .joinLeft(Image)
      .on {
        case ((m, t), i) =>
          t.memberId === i.memberId
      }.filter {
      case ((m, t), i) =>
        m.memberId === id
    }.map {
      case ((m, t), i) => (
        m.name,
        i,
        t.tweetId,
        t.tweet,
        getStatusCount(good, t.tweetId),
        getStatusCount(bad, t.tweetId),
        getCurrentEvaluate(id, t.tweetId)
        )
    }.drop(page * 100).take(100).result

    db.run(dbio).map { tweetInfoList =>
      tweetInfoList.map {
        case (name, icon, tweetId, tweet, goodCount, badCount, currentState) => TweetInfo(
          name,
          icon,
          tweetId,
          tweet,
          goodCount,
          badCount,
          currentState.getOrElse(no)
        )
      }
    }
  }


  /**
    * 他の人のツイートを取得
    *
    * @param myId     Login Member Id
    * @param friendId Other Member Id
    * @return
    */
  def selectFriendTweet(myId: Long, friendId: Long): Future[Seq[TweetInfo]] = {

    val page = 0

    val dbio = Member
      .join(Tweet)
      .on(_.memberId === _.memberId)
      .joinLeft(Image)
      .on {
        case ((m, t), i) =>
          t.memberId === i.memberId
      }.filter {
      case ((m, t), i) =>
        m.memberId === friendId.bind
    }.map {
      case ((m, t), i) => (
        m.name,
        i,
        t.tweetId,
        t.tweet,
        getStatusCount(good, t.tweetId),
        getStatusCount(bad, t.tweetId),
        getCurrentEvaluate(myId, t.tweetId)
        )
    }.drop(page * 100).take(100).result

    db.run(dbio).map { tweetInfoList =>
      tweetInfoList.map {
        case (name, icon, tweetId, tweet, goodCount, badCount, currentState) => TweetInfo(
          name,
          icon,
          tweetId,
          tweet,
          goodCount,
          badCount,
          currentState.getOrElse(no)
        )
      }
    }
  }

  /**
    * フォロワーのツイートを取得(タイムライン)
    *
    * @param id Login Member Id
    * @return
    */
  def selectFollowerTweet(id: Long): Future[Seq[TweetInfo]] = {

    val page = 0

    val dbio = Member
      .join(Tweet)
      .on(_.memberId === _.memberId)
      .joinLeft(Image)
      .on {
        case ((m, t), i) =>
          t.memberId === i.memberId
      }.filter {
      case ((m, t), i) =>
        (m.memberId in getFollowerIdList(id)) || m.memberId === id
    }.map {
      case ((m, t), i) => (
        m.name,
        i,
        t.tweetId,
        t.tweet,
        getStatusCount(good, t.tweetId),
        getStatusCount(bad, t.tweetId),
        getCurrentEvaluate(id, t.tweetId))
    }.drop(page * 100).take(100).result

    db.run(dbio).map { tweetInfoList =>
      tweetInfoList.map {
        case (name, icon, tweetId, tweet, goodCount, badCount, currentState) => TweetInfo(
          name,
          icon,
          tweetId,
          tweet,
          goodCount,
          badCount,
          currentState.getOrElse(no))
      }
    }
  }

  private[this] def getStatusCount(status: String, tweetId: Rep[Long]): Rep[Int] = {
    TweetEvaluate.filter(e => e.evaluateStatus === status && e.tweetId === tweetId).length
  }

  // todo maxは不適切なので､ maxを使わずに Rep[Option[Int]]で返したい｡
  private[this] def getCurrentEvaluate(memberId: Long, tweetId: Rep[Long]): Rep[Option[String]] = {
    TweetEvaluate.filter(e => e.memberId === memberId && e.tweetId === tweetId).map(_.evaluateStatus).max
  }

  private[this] def getFollowerIdList(id: Long): Query[Rep[Long], Long, Seq] = {
    Follow.filter(_.followerId === id).map(_.followedId.asColumnOf[Long])
  }
}