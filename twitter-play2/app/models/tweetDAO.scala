package models

import java.sql.Timestamp
import javax.inject.Inject

import com.google.inject.ImplementedBy
import com.sun.xml.internal.bind.v2.TODO
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import models.Tables._
import slick.driver.MySQLDriver.api._

/**
  * Created by yuki.haneda on 2016/08/03.
  */

@ImplementedBy(classOf[TweetDAOImpl])
trait TweetDAO extends HasDatabaseConfigProvider[JdbcProfile]{
  def selectMyTweet(id: Int): Future[Seq[TweetRow]]

  def selectFollowerTweet(id: Int): Future[Seq[TweetRow]]

  def selectFollowerList(id: Int): Future[Seq[MemberRow]]

  def follow(follow: FollowRow): Unit
}

class TweetDAOImpl @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with TweetDAO{

  def selectMyTweet(id: Int): Future[Seq[TweetRow]] ={
    db.run(Tables.Tweet.filter(_.memberId === id.bind).result)
  }

  def selectFollowerTweet(id: Int): Future[Seq[TweetRow]] = {
    db.run(Tables.Follow.filter(_.followerId === id.bind).flatMap{ a =>
      Tables.Tweet.filter(t => t.memberId === a.followedId || t.memberId === id.bind)
    }.result)
  }

  def selectFollowerList(id: Int): Future[Seq[MemberRow]] = {
    db.run(Tables.Follow.filter(_.followerId === id.bind).flatMap{ a =>
     Tables.Member.filter(m => m.memberId === a.followedId)
    }.result)
  }

  def follow(follow: FollowRow) = {
    db.run(Tables.Follow += follow)
  }

}
