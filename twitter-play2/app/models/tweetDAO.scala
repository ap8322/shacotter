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
  def selectMyTweet(id: Int): Future[Seq[TweetRow]]

  def selectFollowerTweet(id: Int): Future[Seq[TweetRow]]

  def selectFollowerList(id: Int): Future[Seq[MemberRow]]

  def follow(follow: FollowRow): Unit

  def unfollow(follow: FollowRow): Unit

  def isFollow(myId: Int, yourId: Int): Future[Boolean]

  def add(tweet: TweetRow): Unit
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
