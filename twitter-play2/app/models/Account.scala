package models

/**
  * Created by yuki.haneda on 2016/08/01.
  */
import org.mindrot.jbcrypt._
import scalikejdbc._
import scalikejdbc.config._

case class Account(id: Int, email: String, password: String, name: String, role: Role)

object Account extends SQLSyntaxSupport[Account] {

  private val a = syntax("a")

  def apply(a: SyntaxProvider[Account])(rs: WrappedResultSet): Account = autoConstruct(rs, a)

  private val auto = AutoSession

  def authenticate(email: String, password: String): Option[Account] = {
    Option(new Account(1, "", "", "",Role.Administrator));
    //findByEmail(password)
  }

  def findByEmail(email: String)(implicit s: DBSession = auto): Option[Account] = withSQL {
    select.from(Account as a).where.eq(a.name, email)
  }.map(Account(a)).single.apply()

  def findById(id: Int)(implicit s: DBSession = auto): Option[Account] = withSQL {
    select.from(Account as a).where.eq(a.id, id)
  }.map(Account(a)).single.apply()

//  def findAll()(implicit s: DBSession = auto): Seq[Account] = withSQL {
//    select.from(Account as a)
//  }.map(Account(a)).list.apply()
//
//  def create(account: Account)(implicit s: DBSession = auto) {
//    withSQL {
//      import account._
//      val pass = BCrypt.hashpw(account.password, BCrypt.gensalt())
//      insert.into(Account).values(id, email, pass, name, role.toString)
//    }.update.apply()
//  }

}
