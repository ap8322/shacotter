package models.dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import utils.SystemClock

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/31.
  */
class ImageDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with SystemClock {

  def insert(memberId: Long, filename: String, base64data: String) = {
    db.run(Image.map(i => (
      i.memberId,
      i.imageName,
      i.imageData,
      i.registerDatetime,
      i.registerUser,
      i.updateDatetime,
      i.updateUser,
      i.versionNo)
    ) += (
      memberId,
      filename,
      base64data,
      currentTimestamp,
      memberId.toString,
      currentTimestamp,
      memberId.toString,
      1
      )
    )
  }

  // todo 楽観的排他制御
  def update(memberId: Long, filename: String, base64data: String) = {
    db.run(Image.filter(_.memberId === memberId).map(i => (
      i.memberId,
      i.imageName,
      i.imageData,
      i.updateDatetime,
      i.updateUser)
    ).update(
      memberId,
      filename,
      base64data,
      currentTimestamp,
      memberId.toString
    )
    )
  }

  def fetch(memberId: Long): Future[Option[ImageRow]] = {
    db.run(Image.filter(_.memberId === memberId).result.headOption)
  }

}
