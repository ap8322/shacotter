package models.dao

import javax.inject.Inject

import models.Forms.Follower
import models.Tables._, profile.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.Rep
import utils.SystemClock

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/23.
  */
class FollowDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with SystemClock {

//  import dbConfig.driver.api._

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
      .joinLeft(Image).on { case ((m, f), i) => m.memberId === i.memberId }
      .map {
        case ((member: Member, follow: Rep[Option[Follow]]), image: Rep[Option[Image]]) => (
          member.memberId,
          member.name,
          image,
          follow.isDefined
          )
      }.result

    db.run(dbio).map { memberlist =>
      memberlist.map {
        case (memberId, name, icon, isFollowed) =>
          Follower(
            memberId,
            name,
            icon,
            isFollowed
          )
      }
    }
  }
}