package models.DAO

import com.google.inject.Inject
import com.sun.corba.se.impl.orbutil.closure.Future
import models.Tables.{Eval, EvalRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/18.
  */
class EvalDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def insertStatus(eval: EvalRow): Future[Int] = {
    db.run(Eval += eval)
  }

  def deleteStatus(tweetId: Int, memberId: Int): Future[Int] = {
    db.run(Eval.filter(e => e.tweetId === tweetId && e.memberId === memberId).delete)
  }

  def updateStatus(eval: EvalRow): Future[Int] = {
    db.run(Eval.filter(e => e.tweetId === eval.tweetId && e.memberId === eval.memberId).update(eval))
  }
}
