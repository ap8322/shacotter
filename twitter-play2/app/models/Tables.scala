package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Account.schema ++ Member.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Account
   *  @param id Database column id SqlType(INT), Default(None)
   *  @param email Database column email SqlType(TEXT), Default(None)
   *  @param password Database column password SqlType(TEXT), Default(None)
   *  @param name Database column name SqlType(TEXT), Default(None)
   *  @param role Database column role SqlType(TEXT), Default(None) */
  case class AccountRow(id: Option[Int] = None, email: Option[String] = None, password: Option[String] = None, name: Option[String] = None, role: Option[String] = None)
  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
  implicit def GetResultAccountRow(implicit e0: GR[Option[Int]], e1: GR[Option[String]]): GR[AccountRow] = GR{
    prs => import prs._
    AccountRow.tupled((<<?[Int], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table account. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends Table[AccountRow](_tableTag, "account") {
    def * = (id, email, password, name, role) <> (AccountRow.tupled, AccountRow.unapply)

    /** Database column id SqlType(INT), Default(None) */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.Default(None))
    /** Database column email SqlType(TEXT), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Default(None))
    /** Database column password SqlType(TEXT), Default(None) */
    val password: Rep[Option[String]] = column[Option[String]]("password", O.Default(None))
    /** Database column name SqlType(TEXT), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Default(None))
    /** Database column role SqlType(TEXT), Default(None) */
    val role: Rep[Option[String]] = column[Option[String]]("role", O.Default(None))
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))

  /** Entity class storing rows of table Member
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(TEXT)
   *  @param password Database column password SqlType(TEXT)
   *  @param passwordsalt Database column passwordSalt SqlType(TEXT)
   *  @param name Database column name SqlType(TEXT) */
  case class MemberRow(id: Int, email: String, password: String, passwordsalt: String, name: String)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Int], e1: GR[String]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table member. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends Table[MemberRow](_tableTag, "member") {
    def * = (id, email, password, passwordsalt, name) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(email), Rep.Some(password), Rep.Some(passwordsalt), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(TEXT) */
    val email: Rep[String] = column[String]("email")
    /** Database column password SqlType(TEXT) */
    val password: Rep[String] = column[String]("password")
    /** Database column passwordSalt SqlType(TEXT) */
    val passwordsalt: Rep[String] = column[String]("passwordSalt")
    /** Database column name SqlType(TEXT) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))
}
