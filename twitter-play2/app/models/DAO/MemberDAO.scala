package models.DAO

/**
  * Created by yuki.haneda on 2016/08/03.
  */
import com.google.inject.Inject
import models.Forms._
import models.Tables.Member
import models.Tables.MemberRow
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MemberDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  def authenticate(form: LoginForm): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.email === form.email.bind).result.headOption).map {
      case Some(user) if BCrypt.checkpw(form.password, user.password) => Some(user)
      case _ => None
    }
  }

  def findById(id: Int): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.memberId === id.bind).result.headOption)
  }

  def findByEmail(email: String): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.email === email.bind).result.headOption)
  }

  def create(member: MemberRow): Future[Option[MemberRow]] = {
    db.run(Member += member)
    findByEmail(member.email)
  }

  def update(member: MemberRow): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.memberId === member.memberId).update(member))
    findById(member.memberId)
  }

  def selectList(): Future[Seq[MemberRow]] = {
    db.run(Member.sortBy(m => m.memberId).result)
  }
}