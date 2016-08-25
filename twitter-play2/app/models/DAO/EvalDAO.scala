package models.dao

import javax.inject.Inject

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

  def insert(eval: EvalRow): Future[Int] = {
    db.run(Eval += eval)
  }

  def delete(tweetId: Int, memberId: Int): Future[Int] = {
    db.run(Eval.filter(e => e.tweetId === tweetId && e.memberId === memberId).delete)
  }
  val a = 1 :: 2 :: 3 :: Nil

  def update(eval: EvalRow): Future[Int] = {
    db.run(Eval.filter(e => e.tweetId === eval.tweetId && e.memberId === eval.memberId).update(eval))
  }
}