package models.dao

import javax.inject.Inject

import models.Forms.Follower
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.Rep

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/23.
  */
class FollowDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def follow(myId: Int, followId: Int): Future[Int] = {
    db.run(Follow += FollowRow(myId, followId))
  }

  def remove(myId: Int, followId: Int): Future[Int] = {
    db.run(Follow.filter(f => f.followerId === myId && f.followedId === followId.bind).delete)
  }

  def selectFollowerList(id: Int): Future[Seq[Follower]] = {

    val dbio = (Member joinLeft Follow on (_.memberId === _.followedId) map {
      case (member: Member, follow: Rep[Option[Follow]]) => {
        (member.memberId, member.name, follow.map(_.followerId === id))
      }
    }).result

    // Rep[Option[Follow]]のままでは扱い辛いのでisFollowedは外側で対処
    db.run(dbio).map { memberlist =>
      memberlist.map {
        case (memberId, name, isFollowed) => {
          Follower(memberId, name, isFollowed.getOrElse(false))
        }
      }
    }
  }
}