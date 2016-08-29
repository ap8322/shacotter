package models.dao

/**
  * Created by yuki.haneda on 2016/08/03.
  */

import javax.inject.Inject

import models.Tables.{Member, MemberRow}
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MemberDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def authenticate(email: String, password: String): Future[Either[String, MemberRow]] = {
    db.run(Member.filter(_.email === email.bind).result.headOption).map {
      case Some(user) if BCrypt.checkpw(password, user.password) => Right(user)
      case _ => Left("メールまたはパスワードが違います｡")
    }
  }

  def findById(id: Int): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.memberId === id.bind).result.headOption)
  }

  def findByEmail(email: String): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.email === email.bind).result.headOption)
  }

  def create(email: String, password: String, name: String): Future[Int] = {
    db.run(Member.map(m => (m.name, m.password, m.name)) += (email, hashPassword(password), name))
  }

  def update(id: Int, email: String, password: String, name: String): Future[Int] = {
    val member = MemberRow(id, email, hashPassword(password), name)
    db.run(Member.filter(_.memberId === member.memberId).update(member))
  }

  def selectList(): Future[Seq[MemberRow]] = {
    db.run(Member.sortBy(m => m.memberId).result)
  }

  private[this] def hashPassword(rawPasword: String): String = {
    BCrypt.hashpw(rawPasword, BCrypt.gensalt())
  }
}