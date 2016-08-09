package models

import javax.inject.Inject

import com.google.inject.ImplementedBy
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/03.
  */
@ImplementedBy(classOf[TweetDAOImpl])
trait TweetDAO extends HasDatabaseConfigProvider[JdbcProfile] {
  def selectMyTweet(id: Int): Future[Seq[TweetRow]]

  def selectFollowerTweet(id: Int): Future[Seq[TweetRow]]

  def selectFollowerList(id: Int): Future[Seq[MemberRow]]

  def follow(follow: FollowRow): Unit

  def add(tweet: TweetRow):Unit
}

class TweetDAOImpl @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with TweetDAO {

  def selectMyTweet(id: Int): Future[Seq[TweetRow]] = {
    db.run(Tables.Tweet.filter(_.memberId === id.bind).result)
  }

  def selectFollowerTweet(id: Int): Future[Seq[TweetRow]] = {
    db.run(Tables.Follow.filter(_.followerId === id.bind).flatMap { f =>
      Tables.Tweet.filter(t => t.memberId === f.followedId)
    }.result)
  }

  def selectFollowerList(id: Int): Future[Seq[MemberRow]] = {
    db.run(Tables.Follow.filter(_.followerId === id.bind).flatMap { f =>
      Tables.Member.filter(m => m.memberId === f.followedId)
    }.result)
  }

  def follow(follow: FollowRow) = {
    db.run(Tables.Follow += follow)
  }

  def add(tweet: TweetRow): Unit = {
    db.run(Tables.Tweet += tweet)
  }
}
