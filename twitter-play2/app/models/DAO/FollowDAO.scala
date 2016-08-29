package models.dao

import javax.inject.Inject

import models.Forms.Follower
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.Rep
import utils.SystemClock

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/23.
  */
class FollowDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with SystemClock {

  def follow(myId: Long, followId: Long): Future[Int] = {
    db.run(
      Follow += FollowRow(
        myId,
        followId,
        currentTimestamp,
        myId.toString,
        currentTimestamp,
        myId.toString,
        1.toLong)
    )
  }

  def remove(myId: Long, followId: Long): Future[Int] = {
    db.run(
      Follow.filter(f =>
        f.followerId === myId && f.followedId === followId.bind
      ).delete
    )
  }

  def selectFollowerList(id: Long): Future[Seq[Follower]] = {

    val dbio = Member
      .joinLeft(Follow)
      .on((m, f) => m.memberId === f.followedId && f.followerId === id)
      .map {
        case (member: Member, follow: Rep[Option[Follow]]) => (
          member.memberId,
          member.name,
          follow.isDefined
          )
      }.result

    db.run(dbio).map { memberlist =>
      memberlist.map {
        case (memberId, name, isFollowed) =>
          Follower(
            memberId,
            name,
            isFollowed
          )
      }
    }
  }
}