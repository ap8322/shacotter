package models

/**
  * Created by yuki.haneda on 2016/08/03.
  */

import java.util.UUID
import javax.inject.Inject

import com.google.inject.ImplementedBy
import models.Tables.MemberRow
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Member(id: Int, email: String, password: String, name: String) {
  def this(row: MemberRow) = this(row.memberId, row.email, row.password, row.name)
}

@ImplementedBy(classOf[MemberDAOImpl])
trait MemberDAO extends HasDatabaseConfigProvider[JdbcProfile] {
  def authenticate(form: LoginForm): Future[Option[MemberRow]]

  def selectList(): Future[Seq[(MemberRow)]]

  def findById(id: Int): Future[Option[MemberRow]]

  def findByEmail(email: String): Future[Option[MemberRow]]

  def create(member: MemberRow): Future[Option[MemberRow]]

  def update(member: MemberRow): Future[Option[MemberRow]]
}

class MemberDAOImpl @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with MemberDAO {

  def authenticate(form: LoginForm): Future[Option[MemberRow]] = {
    db.run(Tables.Member.filter(_.email === form.email.bind).result.headOption).map {
      case Some(user) if form.password == user.password => Some(user)
      case _ => None
    }
  }

  def findById(id: Int): Future[Option[MemberRow]] = {
    db.run(Tables.Member.filter(_.memberId === id.bind).result.headOption)
  }

  def findByEmail(email: String): Future[Option[MemberRow]] = {
    db.run(Tables.Member.filter(_.email === email.bind).result.headOption)
  }

  /**
    * インサートした後にそのメンバーを返してくれる｡
    *
    * @param member
    * @return
    */
  def create(member: MemberRow): Future[Option[MemberRow]] = {
    db.run(Tables.Member += member)
    findByEmail(member.email)
  }

  def update(member: MemberRow): Future[Option[MemberRow]] = {
    db.run(Tables.Member.filter(_.memberId === member.memberId).update(member))
    findById(member.memberId)
  }

  def selectList(): Future[Seq[MemberRow]] = {
    db.run(Tables.Member.sortBy(m => m.memberId).result)
  }
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
