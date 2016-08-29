package models.dao

import java.sql.Timestamp
import java.util.Date
import javax.inject.Inject

import models.Forms.TweetInfo
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/03.
  *
  */

// done ベタ書きSQLから卒業する｡
// done slick で書きなおした部分でいいね､どうでもいいねどちらも0のものだけ､検索されない｡
// todo 共通部分が多いので冗長部分をなくす｡

class TweetDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  /**
    * ツイートを追加
    *
    * @param tweet
    */
  def add(myId: Int, myTweet: String): Future[Int] = {
    db.run(Tweet.map(t => (t.memberId, t.tweet, t.tweetAt)) += (Some(myId), Some(myTweet), new Timestamp(new Date().getTime)))
  }

  /**
    * 自分のツイートを取得
    *
    * @param id Login Member Id
    * @return
    */
  def selectMyTweet(id: Int): Future[Seq[TweetInfo]] = {
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

    val dbio = Member
      .join(Tweet)
      .on(_.memberId === _.memberId)
      .joinLeft(Eval)
      .on {
        case ((m, t), e) =>
          t.tweetId === e.tweetId
      }.filter {
      case ((m, t), e) =>
        m.memberId === id
    }.map {
      case ((m, t), e) => (
        m.name,
        t.tweetId,
        t.tweet,
        getStatusCount(1, t.tweetId),
        getStatusCount(0, t.tweetId),
        getCurrentEvaluete(id, t.tweetId)
        )
    }.result

    db.run(dbio).map { tweetInfoList =>
      tweetInfoList.map {
        // tweet.get は本来not null なのですが､codegenで生成した時にnot null制約が抜けていたので今は一旦getにしています｡
        // 共通カラム追加時のcodegenをした後に修正します｡
        case (name, tweetId, tweet, goodCount, badCount, currentState) => TweetInfo(
          name,
          tweetId,
          tweet.get,
          goodCount,
          badCount,
          currentState.getOrElse(-1)
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
  def selectFriendTweet(myId: Int, friendId: Int): Future[Seq[TweetInfo]] = {
    val dbio = Member
      .join(Tweet)
      .on(_.memberId === _.memberId)
      .joinLeft(Eval)
      .on {
        case ((m, t), e) =>
          t.tweetId === e.tweetId
      }.filter {
      case ((m, t), e) =>
        m.memberId === friendId.bind
    }.map {
      case ((m, t), e) => (
        m.name,
        t.tweetId,
        t.tweet,
        getStatusCount(1, t.tweetId),
        getStatusCount(0, t.tweetId),
        getCurrentEvaluete(myId, t.tweetId)
        )
    }.result

    db.run(dbio).map { tweetInfoList =>
      tweetInfoList.map {
        case (name, tweetId, tweet, goodCount, badCount, currentState) => TweetInfo(
          name,
          tweetId,
          tweet.get,
          goodCount,
          badCount,
          currentState.getOrElse(-1)
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
  def selectFollowerTweet(id: Int): Future[Seq[TweetInfo]] = {
    val dbio = Member
      .join(Tweet)
      .on(_.memberId === _.memberId)
      .joinLeft(Eval)
      .on {
        case ((m, t), e) =>
          t.tweetId === e.tweetId
      }.filter {
      case ((m, t), e) =>
        (m.memberId in getFollowerIdList(id)) || m.memberId === id
    }.map {
      case ((m, t), e) => (
        m.name,
        t.tweetId,
        t.tweet,
        getStatusCount(1, t.tweetId),
        getStatusCount(0, t.tweetId),
        getCurrentEvaluete(id, t.tweetId))
    }.result

    db.run(dbio).map { tweetInfoList =>
      tweetInfoList.map {
        case (name, tweetId, tweet, goodCount, badCount, currentState) => TweetInfo(
          name,
          tweetId,
          tweet.get,
          goodCount,
          badCount,
          currentState.getOrElse(-1))
      }
    }
  }

  private[this] def getStatusCount(status: Int, tweetId: Rep[Int]): Rep[Int] = {
    Eval.filter(e => e.evalStatus === status && e.tweetId === tweetId).length
  }

  // todo maxは不適切なので､ maxを使わずに Rep[Option[Int]]で返したい｡
  private[this] def getCurrentEvaluete(memberId: Int, tweetId: Rep[Int]): Rep[Option[Int]] = {
    Eval.filter(e => e.memberId === memberId && e.tweetId === tweetId).map(_.evalStatus).max
  }

  private[this] def getFollowerIdList(id: Int): Query[Rep[Int], Int, Seq] = {
    Follow.filter(_.followerId === id).map(_.followedId.asColumnOf[Int])
  }
}