package controllers.auth

/**
  * Created by yuki.haneda on 2016/08/03.
  */

import java.util.UUID
import javax.inject.Inject

import com.google.inject.ImplementedBy
import models.Tables
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.driver.MySQLDriver.api._

@ImplementedBy(classOf[MemberDAO])
trait MemberDAOLike extends HasDatabaseConfigProvider[JdbcProfile] {
  def findByEmail(email: String): Future[Option[Member]]

  def authenticate(form: LoginForm): Future[Option[Member]]

  def findById(id: Int): Future[Option[Member]]

  def create(member: Tables.MemberRow): Future[Option[Member]]
}

class MemberDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with MemberDAOLike {

  import MemberDAO._

  def findByEmail(email: String): Future[Option[Member]] = {
    db.run(Tables.Member.filter(_.email === email.bind).result.headOption).map { user =>
      Some(new Member(user.get))
    }
  }

  def authenticate(form: LoginForm): Future[Option[Member]] = {
    db.run(Tables.Member.filter(_.email === form.email.bind).result.headOption).map {
      case Some(user) if form.password == user.password => Some(new Member(user))
      case _ => None
    }
  }

  def findById(id: Int): Future[Option[Member]] = {
    db.run(Tables.Member.filter(_.id === id.bind).result.headOption).map { user =>
      Some(new Member(user.get))
    }
  }

  /**
    * インサートした後にそのメンバーを返してくれる｡
    * @param member
    * @return
    */
  def create(member: Tables.MemberRow): Future[Option[Member]]  = {
    db.run(Tables.Member+=member)
    findByEmail(member.email)
  }


//  private def selectByEmail(email: String): Future[Tables.MemberRow] = {
//    db.run(Tables.Member.filter(_.email === email.bind).result.head)
//  }
}

object MemberDAO {

  val STRETCH_LOOP_COUNT = 1000

  def hashAndStretch(plain: String, salt: String, loopCnt: Int): String = {
    var hashed: String = ""
    (1 to STRETCH_LOOP_COUNT).foreach(i =>
      hashed = DigestUtils.sha256Hex(hashed + plain + salt)
    )
    hashed
  }

  def createPasswordSalt(): String = {
    DigestUtils.sha256Hex(UUID.randomUUID().toString())
  }
}
