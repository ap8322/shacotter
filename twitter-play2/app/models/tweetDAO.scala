package models

import javax.inject.Inject

import com.google.inject.ImplementedBy
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/03.
  */
@ImplementedBy(classOf[TweetDAOImpl])
trait TweetDAO extends HasDatabaseConfigProvider[JdbcProfile] {
  def selectMyTweet(id: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]]

  def selectFriendTweet(myId: Int, friendId: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]]

  def selectFollowerTweet(id: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]]

  def selectFollowerList(id: Int): Future[Vector[(Int, String, Option[Int])]]

  def follow(follow: FollowRow): Unit

  def unfollow(follow: FollowRow): Unit

  def isFollow(myId: Int, yourId: Int): Future[Boolean]

  def add(tweet: TweetRow): Unit
}

class TweetDAOImpl @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with TweetDAO {

  def selectMyTweet(id: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]] = {
    //tweetに欲しい情報
    //名前､tweet､時間､いいね数､どうでもいいね数
    //   for {
    //     (t,m) <- Tweet joinLeft
    //   }
    val a = sql"""SELECT m.name,
                    t.tweet_id,
                    t.tweet,
                    count(e.eval_status = 1 OR NULL) AS good,
                    count(e.eval_status = 0 OR NULL) AS bad,
                   (select eval_status from Eval where member_id = $id and tweet_id = t.tweet_id)
                  FROM Member m
                    LEFT JOIN Tweet t ON m.member_id = t.member_id
                    LEFT JOIN Eval e ON t.tweet_id = e.tweet_id
                  WHERE m.member_id = $id
                  GROUP BY t.tweet_id;""".as[(String, Int, String, Int, Int, Option[Int])]

    //    ((Member join Tweet on (_.memberId === _.memberId)) join Eval on (_._2.tweetId === _.tweetId)).filter(_._1._1.memberId === id)
    //      .groupBy(_._1._2.tweetId).map {
    //      case (rep, query) => (rep, query.map(_._2))
    //    }

    db.run(a)
    //    db.run(Tables.Tweet.filter(_.memberId === id.bind).result)

    //    run
  }

  def selectFriendTweet(myId: Int, friendId: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]] = {
    val a = sql"""SELECT m.name,
                    t.tweet_id,
                    t.tweet,
                    count(e.eval_status = 1 OR NULL) AS good,
                    count(e.eval_status = 0 OR NULL) AS bad,
                   (select eval_status from Eval where member_id = $myId and tweet_id = t.tweet_id)
                  FROM Member m
                    LEFT JOIN Tweet t ON m.member_id = t.member_id
                    LEFT JOIN Eval e ON t.tweet_id = e.tweet_id
                  WHERE m.member_id = $friendId
                  GROUP BY t.tweet_id;""".as[(String, Int, String, Int, Int, Option[Int])]
    db.run(a)
  }

  def selectFollowerTweet(id: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]] = {

    val a = sql"""SELECT m.name,
                    t.tweet_id,
                    t.tweet,
                    count(e.eval_status = 1 OR NULL) AS good,
                    count(e.eval_status = 0 OR NULL) AS bad,
                  (select eval_status from Eval where member_id = $id and tweet_id = t.tweet_id)
                  FROM Member m
                    LEFT JOIN Tweet t ON m.member_id = t.member_id
                    LEFT JOIN Eval e ON t.tweet_id = e.tweet_id
                  WHERE m.member_id in (SELECT f.followed_id FROM Follow f WHERE f.follower_id = $id) OR m.member_id = $id
                  GROUP BY t.tweet_id,m.member_id;""".as[(String, Int, String, Int, Int, Option[Int])]

    db.run(a)
  }

  def selectFollowerList(id: Int): Future[Vector[(Int, String, Option[Int])]] = {

    //フォローしている人の名前､ID､フォロしているかどうか｡

    val a =
      sql"""SELECT
  m.member_id,
  m.name,
  (SELECT m.member_id
   FROM Member mem
     JOIN Follow f ON mem.member_id = f.follower_id
   WHERE f.follower_id = $id AND f.followed_id = m.member_id) AS isFollow
FROM Member m""".as[(Int, String, Option[Int])]

    //    db.run(Tables.Follow.filter(_.followerId === id.bind).flatMap { f =>
    //      Tables.Member.filter(m => m.memberId === f.followedId)
    //    }.result)
    db.run(a)
  }

  def follow(follow: FollowRow) = {
    db.run(Tables.Follow += follow)
  }

  def unfollow(follow: FollowRow) = {
    db.run(Tables.Follow.filter(_.followedId === follow.followedId)
      .filter(_.followerId === follow.followerId).delete)
  }

  def isFollow(myId: Int, yourId: Int): Future[Boolean] = {
    db.run(Tables.Follow.filter(_.followerId === myId)
      .filter(_.followedId === yourId).result.headOption).map {
      case Some(_) => true
      case None => false
    }
  }

  def add(tweet: TweetRow): Unit = {
    db.run(Tables.Tweet += tweet)
  }
}