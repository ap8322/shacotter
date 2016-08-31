package models.dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/31.
  */
class ImageDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def insert(memberId: Long, filename: String, base64data: String) = {
    db.run(Image.map(i => (i.memberId, i.imageName, i.imageData)) += (
      memberId,
      filename,
      base64data
      )
    )
  }

  def fetch(memberId: Long): Future[Option[ImageRow]] = {
    db.run(Image.filter(_.memberId === memberId).result.headOption)
  }

}
