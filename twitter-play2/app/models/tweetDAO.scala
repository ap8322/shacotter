package models

import javax.inject.Inject

import com.google.inject.ImplementedBy
import com.sun.xml.internal.bind.v2.TODO
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import slick.driver.MySQLDriver.api._

/**
  * Created by yuki.haneda on 2016/08/03.
  */
@ImplementedBy(classOf[tweetDAOImpl])
trait tweetDAO extends HasDatabaseConfigProvider[JdbcProfile]{
  def selectTweetList(name : String): Future[Seq[String]]


}

class tweetDAOImpl @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with tweetDAO{
  override def selectTweetList(name: String): Future[Seq[String]] = ???


}
