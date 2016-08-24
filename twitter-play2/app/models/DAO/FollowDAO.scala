package models.DAO

import com.google.inject.Inject
import models.Tables.{Follow, _}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/23.
  */
class FollowDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def follow(follow: FollowRow): Future[Int] = {
    db.run(Follow += follow)
  }

  def remove(follow: FollowRow): Future[Int] = {
   db.run(Follow.filter(f => f.followerId === follow.followerId && f.followedId === follow.followedId).delete)
  }

  case class Follower(memberId: Int, memberName: String, isFollowed: Boolean)
  def selectFollowerList(id: Int): Future[Vector[(Int, String, Option[Int])]] = {

    //フォローしている人の名前､ID､フォロしているかどうか｡

    val a = sql"""SELECT
          m.member_id,
          m.name,
          (SELECT m.member_id
                  FROM Member mem
                  JOIN Follow f ON mem.member_id = f.follower_id
                   WHERE f.follower_id = $id AND f.followed_id = m.member_id) AS isFollow
          FROM Member m""".as[(Int, String, Option[Int])]

    db.run(a).map { followers.map{ f => Follower(f._1, f._2, f._3.isDefined)}}
  }
}(())