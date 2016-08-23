package models.DAO


import com.google.inject.Inject
import models.Tables
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

// todo ベタ書きSQLから卒業する｡
class TweetDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  /**
    * ツイートを追加
    * @param tweet
    */
  def add(tweet: TweetRow): Unit = {
    db.run(Tweet += tweet)
  }

  /**
    * 自分のツイートを取得
    * @param Login_Member_Id
    * @return
    */
  def selectMyTweet(id: Int): Future[Vector[(String, Int, String, Int, Int, Option[Int])]] = {
    val a = sql"""SELECT m.name,
                    t.tweet_id,
                    t.tweet,
                    (select COUNT(*) FROM Eval se1 WHERE eval_status = 1 AND se1.tweet_id = t.tweet_id),
                    (select COUNT(*) FROM Eval se2 WHERE eval_status = 0 AND se2.tweet_id = t.tweet_id),
                    (select eval_status from Eval where member_id = $id and tweet_id = t.tweet_id)
                  FROM Member m
                    LEFT JOIN Tweet t ON m.member_id = t.member_id
                    LEFT JOIN Eval e ON t.tweet_id = e.tweet_id
                  WHERE m.member_id = $id;""".as[(String, Int, String, Int, Int, Option[Int])]

    Member.join(Tweet).on(_.memberId === _.memberId).join(Eval).on { case ((m, t), e) => t.tweetId === e.tweetId }

    val result = Tweet.filter(_.memberId === 1).result

    db.run(a)
  }

  /**
    * 他の人のツイートを取得
    * @param Login_Member_Id
    * @param Other_Member_Id
    * @return
    */
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

    (for {m <- Member;
          t <- Tweet if m.memberId === t.memberId;
          e <- Eval if t.tweetId === e.tweetId;
          good <- Eval if good.evalStatus === 1 && good.tweetId === t.tweetId;
          bad <- Eval if bad.evalStatus === 0 && bad.tweetId === t.tweetId
    } yield (m.name, t.tweetId, t.tweet, good, bad)).result

    db.run(a)
  }

  /**
    * フォロワーのツイートを取得(タイムライン)
    * @param Login_Member_Id
    * @return
    */
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
}