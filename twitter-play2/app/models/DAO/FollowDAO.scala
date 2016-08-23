package models.DAO

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import models.Tables._
import models.Tables.Follow
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/23.
  */
class FollowDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def follow(follow: FollowRow) = {
    db.run(Follow += follow)
  }

  def remove(follow: FollowRow) = {
    db.run(Follow.filter(_.followedId === follow.followedId)
      .filter(_.followerId === follow.followerId).delete)
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

    db.run(a)
  }
}
