package models

import javax.inject.Inject

import com.google.inject.ImplementedBy
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/18.
  */
@ImplementedBy(classOf[EvalDAOImpl])
trait EvalDAO extends HasDatabaseConfigProvider[JdbcProfile] {

  def countGood(): Future[Int]

  def insertStatus(eval: EvalRow): Unit

  def deleteStatus(tweetId: Int, memberId: Int): Unit

  def updateStatus(eval: EvalRow): Unit
}

class EvalDAOImpl @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with EvalDAO {

  def insertStatus(eval: EvalRow): Unit = {
    db.run(Eval += eval)
  }

  def deleteStatus(tweetId: Int, memberId: Int): Unit = {
    db.run(Eval.filter(e => e.tweetId === tweetId && e.memberId === memberId).delete)
  }

  def updateStatus(eval: EvalRow): Unit = {
    db.run(Eval.filter(e => e.tweetId === eval.tweetId && e.memberId === eval.memberId).update(eval))
  }

  def countGood(): Future[Int] = {
    db.run(Eval.filter(_.evalStatus === 1).length.result)
  }
}
