package models.dao

/**
  * Created by yuki.haneda on 2016/08/03.
  */

import javax.inject.Inject

import models.Forms.MemberInfo
import models.Tables.{Member, MemberRow}
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import utils.SystemClock

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MemberDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                          val imageDAO: ImageDAO)
  extends HasDatabaseConfigProvider[JdbcProfile] with SystemClock {

  def authenticate(email: String, password: String): Future[Either[String, MemberRow]] = {
    db.run(Member.filter(_.email === email.bind).result.headOption).map {
      case Some(user) if BCrypt.checkpw(password, user.password) => Right(user)
      case _ => Left("メールまたはパスワードが違います｡")
    }
  }

  def findById(id: Long): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.memberId === id.bind).result.headOption)
  }

  def findByEmail(email: String): Future[Option[MemberRow]] = {
    db.run(Member.filter(_.email === email.bind).result.headOption)
  }

  def selectMemberInfo(memberId: Long): Future[MemberInfo] = {
    for {
      imageData <- imageDAO.fetch(memberId)
      memberData <- findById(memberId)
    } yield MemberInfo(
      memberData.get.memberId,
      memberData.get.name,
      imageData
    )
  }

  def create(email: String, password: String, name: String): Future[Int] = {
    db.run(Member.map(m =>
      (
        m.email,
        m.password,
        m.name,
        m.registerDatetime,
        m.registerUser,
        m.updateDatetime,
        m.updateUser,
        m.versionNo
        )
    ) += (
      email,
      hashPassword(password),
      name,
      currentTimestamp,
      name,
      currentTimestamp,
      name,
      1.toLong))
  }

  def update(id: Long, email: String, password: String, name: String): Future[Int] = {
    val member = MemberRow(
      id,
      email,
      hashPassword(password),
      name,
      currentTimestamp,
      name,
      currentTimestamp,
      name,
      1.toLong
    )

    db.run(Member.filter(_.memberId === member.memberId).update(member))
  }

  private[this] def hashPassword(rawPasword: String): String = {
    BCrypt.hashpw(rawPasword, BCrypt.gensalt())
  }
}