package models

/**
  * Created by yuki.haneda on 2016/08/02.
  */
import scala.concurrent.Future

import models.Account
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

class AccountDAO extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  private val Accounts = TableQuery[AccountsTable]

  private class AccountsTable(tag: Tag) extends Table[Account](tag, "account") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")
    def password = column[String]("password")
    def name = column[String]("name")
    def role = column[String]("role")

    def * = (id.?, email, password, name, role) <> (Account.tupled, Account.unapply _)
  }

  def authenticate( email: String, password: String ): Option[(String, String)] = {
    val result = findByEmail( email ).filter( account => BCrypt.checkpw( password, account.map( _.password ).getOrElse( "" ) ) )

    Await.result(result, 5 seconds).map{u => (u.email, u.password)}
  }

  def findByEmail( email: String ): Future[Option[Account]] =
    db.run( Accounts.filter(_.email === email).result.headOption)

  def findById(id: Long): Future[Option[Account]] =
    db.run(Accounts.filter(_.id === id).result.headOption)

  def all(): Future[List[Account]] = db.run(Accounts.result).map(_.toList)

  def insert(account: Account): Future[Account] = db.run(Accounts += account).map(_ => account)

  def isEmpty: Boolean = !Await.result(db.run(Accounts.exists.result), Duration.Inf)
}
