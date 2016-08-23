package models.DAO

import com.google.inject.Inject
import models.Tables.{Eval,EvalRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

/**
  * Created by yuki.haneda on 2016/08/18.
  */
class EvalDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def insertStatus(eval: EvalRow): Unit = {
    db.run(Eval += eval)
  }

  def deleteStatus(tweetId: Int, memberId: Int): Unit = {
    db.run(Eval.filter(e => e.tweetId === tweetId && e.memberId === memberId).delete)
  }

  def updateStatus(eval: EvalRow): Unit = {
    db.run(Eval.filter(e => e.tweetId === eval.tweetId && e.memberId === eval.memberId).update(eval))
  }
}
