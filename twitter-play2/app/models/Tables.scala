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
  lazy val schema: profile.SchemaDescription = Array(Member.schema, MemberAddress.schema, MemberFollowing.schema, MemberLogin.schema, MemberSecurity.schema, MemberService.schema, MemberStatus.schema, MemberWithdrawal.schema, Product.schema, ProductCategory.schema, ProductStatus.schema, Purchase.schema, PurchasePayment.schema, Region.schema, ServiceRank.schema, WithdrawalReason.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Member
   *  @param memberId Database column MEMBER_ID SqlType(INT), AutoInc, PrimaryKey
   *  @param memberName Database column MEMBER_NAME SqlType(VARCHAR), Length(160,true)
   *  @param memberAccount Database column MEMBER_ACCOUNT SqlType(VARCHAR), Length(50,true)
   *  @param memberStatusCode Database column MEMBER_STATUS_CODE SqlType(CHAR), Length(3,false)
   *  @param formalizedDatetime Database column FORMALIZED_DATETIME SqlType(DATETIME), Default(None)
   *  @param birthdate Database column BIRTHDATE SqlType(DATE), Default(None)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberRow(memberId: Int, memberName: String, memberAccount: String, memberStatusCode: String, formalizedDatetime: Option[java.sql.Timestamp] = None, birthdate: Option[java.sql.Date] = None, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[java.sql.Date]], e4: GR[java.sql.Timestamp], e5: GR[Long]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<?[java.sql.Timestamp], <<?[java.sql.Date], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table MEMBER. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends Table[MemberRow](_tableTag, "MEMBER") {
    def * = (memberId, memberName, memberAccount, memberStatusCode, formalizedDatetime, birthdate, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(memberName), Rep.Some(memberAccount), Rep.Some(memberStatusCode), formalizedDatetime, birthdate, Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ID SqlType(INT), AutoInc, PrimaryKey */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_NAME SqlType(VARCHAR), Length(160,true) */
    val memberName: Rep[String] = column[String]("MEMBER_NAME", O.Length(160,varying=true))
    /** Database column MEMBER_ACCOUNT SqlType(VARCHAR), Length(50,true) */
    val memberAccount: Rep[String] = column[String]("MEMBER_ACCOUNT", O.Length(50,varying=true))
    /** Database column MEMBER_STATUS_CODE SqlType(CHAR), Length(3,false) */
    val memberStatusCode: Rep[String] = column[String]("MEMBER_STATUS_CODE", O.Length(3,varying=false))
    /** Database column FORMALIZED_DATETIME SqlType(DATETIME), Default(None) */
    val formalizedDatetime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("FORMALIZED_DATETIME", O.Default(None))
    /** Database column BIRTHDATE SqlType(DATE), Default(None) */
    val birthdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("BIRTHDATE", O.Default(None))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing MemberStatus (database name FK_MEMBER_MEMBER_STATUS) */
    lazy val memberStatusFk = foreignKey("FK_MEMBER_MEMBER_STATUS", memberStatusCode, MemberStatus)(r => r.memberStatusCode, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (formalizedDatetime) (database name IX_MEMBER_FORMALIZED_DATETIME) */
    val index1 = index("IX_MEMBER_FORMALIZED_DATETIME", formalizedDatetime)
    /** Index over (memberName) (database name IX_MEMBER_MEMBER_NAME) */
    val index2 = index("IX_MEMBER_MEMBER_NAME", memberName)
    /** Uniqueness Index over (memberAccount) (database name MEMBER_ACCOUNT) */
    val index3 = index("MEMBER_ACCOUNT", memberAccount, unique=true)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table MemberAddress
   *  @param memberAddressId Database column MEMBER_ADDRESS_ID SqlType(INT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(INT)
   *  @param validBeginDate Database column VALID_BEGIN_DATE SqlType(DATE)
   *  @param validEndDate Database column VALID_END_DATE SqlType(DATE)
   *  @param address Database column ADDRESS SqlType(VARCHAR), Length(200,true)
   *  @param regionId Database column REGION_ID SqlType(INT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberAddressRow(memberAddressId: Int, memberId: Int, validBeginDate: java.sql.Date, validEndDate: java.sql.Date, address: String, regionId: Int, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching MemberAddressRow objects using plain SQL queries */
  implicit def GetResultMemberAddressRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[String], e3: GR[java.sql.Timestamp], e4: GR[Long]): GR[MemberAddressRow] = GR{
    prs => import prs._
    MemberAddressRow.tupled((<<[Int], <<[Int], <<[java.sql.Date], <<[java.sql.Date], <<[String], <<[Int], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table MEMBER_ADDRESS. Objects of this class serve as prototypes for rows in queries. */
  class MemberAddress(_tableTag: Tag) extends Table[MemberAddressRow](_tableTag, "MEMBER_ADDRESS") {
    def * = (memberAddressId, memberId, validBeginDate, validEndDate, address, regionId, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (MemberAddressRow.tupled, MemberAddressRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberAddressId), Rep.Some(memberId), Rep.Some(validBeginDate), Rep.Some(validEndDate), Rep.Some(address), Rep.Some(regionId), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberAddressRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ADDRESS_ID SqlType(INT), AutoInc, PrimaryKey */
    val memberAddressId: Rep[Int] = column[Int]("MEMBER_ADDRESS_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(INT) */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID")
    /** Database column VALID_BEGIN_DATE SqlType(DATE) */
    val validBeginDate: Rep[java.sql.Date] = column[java.sql.Date]("VALID_BEGIN_DATE")
    /** Database column VALID_END_DATE SqlType(DATE) */
    val validEndDate: Rep[java.sql.Date] = column[java.sql.Date]("VALID_END_DATE")
    /** Database column ADDRESS SqlType(VARCHAR), Length(200,true) */
    val address: Rep[String] = column[String]("ADDRESS", O.Length(200,varying=true))
    /** Database column REGION_ID SqlType(INT) */
    val regionId: Rep[Int] = column[Int]("REGION_ID")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_MEMBER_ADDRESS_MEMBER) */
    lazy val memberFk = foreignKey("FK_MEMBER_ADDRESS_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Region (database name FK_MEMBER_ADDRESS_REGION) */
    lazy val regionFk = foreignKey("FK_MEMBER_ADDRESS_REGION", regionId, Region)(r => r.regionId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (memberId,validBeginDate) (database name MEMBER_ID) */
    val index1 = index("MEMBER_ID", (memberId, validBeginDate), unique=true)
  }
  /** Collection-like TableQuery object for table MemberAddress */
  lazy val MemberAddress = new TableQuery(tag => new MemberAddress(tag))

  /** Entity class storing rows of table MemberFollowing
   *  @param memberFollowingId Database column MEMBER_FOLLOWING_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param myMemberId Database column MY_MEMBER_ID SqlType(INT)
   *  @param yourMemberId Database column YOUR_MEMBER_ID SqlType(INT)
   *  @param followDatetime Database column FOLLOW_DATETIME SqlType(DATETIME) */
  case class MemberFollowingRow(memberFollowingId: Long, myMemberId: Int, yourMemberId: Int, followDatetime: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberFollowingRow objects using plain SQL queries */
  implicit def GetResultMemberFollowingRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[java.sql.Timestamp]): GR[MemberFollowingRow] = GR{
    prs => import prs._
    MemberFollowingRow.tupled((<<[Long], <<[Int], <<[Int], <<[java.sql.Timestamp]))
  }
  /** Table description of table MEMBER_FOLLOWING. Objects of this class serve as prototypes for rows in queries. */
  class MemberFollowing(_tableTag: Tag) extends Table[MemberFollowingRow](_tableTag, "MEMBER_FOLLOWING") {
    def * = (memberFollowingId, myMemberId, yourMemberId, followDatetime) <> (MemberFollowingRow.tupled, MemberFollowingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberFollowingId), Rep.Some(myMemberId), Rep.Some(yourMemberId), Rep.Some(followDatetime)).shaped.<>({r=>import r._; _1.map(_=> MemberFollowingRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_FOLLOWING_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberFollowingId: Rep[Long] = column[Long]("MEMBER_FOLLOWING_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MY_MEMBER_ID SqlType(INT) */
    val myMemberId: Rep[Int] = column[Int]("MY_MEMBER_ID")
    /** Database column YOUR_MEMBER_ID SqlType(INT) */
    val yourMemberId: Rep[Int] = column[Int]("YOUR_MEMBER_ID")
    /** Database column FOLLOW_DATETIME SqlType(DATETIME) */
    val followDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("FOLLOW_DATETIME")

    /** Foreign key referencing Member (database name FK_MEMBER_FOLLOWING_MY_MEMBER) */
    lazy val memberFk1 = foreignKey("FK_MEMBER_FOLLOWING_MY_MEMBER", myMemberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Member (database name FK_MEMBER_FOLLOWING_YOUR_MEMBER) */
    lazy val memberFk2 = foreignKey("FK_MEMBER_FOLLOWING_YOUR_MEMBER", yourMemberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (followDatetime) (database name IX_MEMBER_FOLLOWING_FOLLOW_DATETIME) */
    val index1 = index("IX_MEMBER_FOLLOWING_FOLLOW_DATETIME", followDatetime)
    /** Index over (yourMemberId,myMemberId) (database name IX_MEMBER_FOLLOWING_UNIQUE_REVERSE) */
    val index2 = index("IX_MEMBER_FOLLOWING_UNIQUE_REVERSE", (yourMemberId, myMemberId))
    /** Uniqueness Index over (myMemberId,yourMemberId) (database name MY_MEMBER_ID) */
    val index3 = index("MY_MEMBER_ID", (myMemberId, yourMemberId), unique=true)
  }
  /** Collection-like TableQuery object for table MemberFollowing */
  lazy val MemberFollowing = new TableQuery(tag => new MemberFollowing(tag))

  /** Entity class storing rows of table MemberLogin
   *  @param memberLoginId Database column MEMBER_LOGIN_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(INT)
   *  @param loginDatetime Database column LOGIN_DATETIME SqlType(DATETIME)
   *  @param mobileLoginFlg Database column MOBILE_LOGIN_FLG SqlType(INT)
   *  @param loginMemberStatusCode Database column LOGIN_MEMBER_STATUS_CODE SqlType(CHAR), Length(3,false) */
  case class MemberLoginRow(memberLoginId: Long, memberId: Int, loginDatetime: java.sql.Timestamp, mobileLoginFlg: Int, loginMemberStatusCode: String)
  /** GetResult implicit for fetching MemberLoginRow objects using plain SQL queries */
  implicit def GetResultMemberLoginRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[java.sql.Timestamp], e3: GR[String]): GR[MemberLoginRow] = GR{
    prs => import prs._
    MemberLoginRow.tupled((<<[Long], <<[Int], <<[java.sql.Timestamp], <<[Int], <<[String]))
  }
  /** Table description of table MEMBER_LOGIN. Objects of this class serve as prototypes for rows in queries. */
  class MemberLogin(_tableTag: Tag) extends Table[MemberLoginRow](_tableTag, "MEMBER_LOGIN") {
    def * = (memberLoginId, memberId, loginDatetime, mobileLoginFlg, loginMemberStatusCode) <> (MemberLoginRow.tupled, MemberLoginRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberLoginId), Rep.Some(memberId), Rep.Some(loginDatetime), Rep.Some(mobileLoginFlg), Rep.Some(loginMemberStatusCode)).shaped.<>({r=>import r._; _1.map(_=> MemberLoginRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_LOGIN_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberLoginId: Rep[Long] = column[Long]("MEMBER_LOGIN_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(INT) */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID")
    /** Database column LOGIN_DATETIME SqlType(DATETIME) */
    val loginDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("LOGIN_DATETIME")
    /** Database column MOBILE_LOGIN_FLG SqlType(INT) */
    val mobileLoginFlg: Rep[Int] = column[Int]("MOBILE_LOGIN_FLG")
    /** Database column LOGIN_MEMBER_STATUS_CODE SqlType(CHAR), Length(3,false) */
    val loginMemberStatusCode: Rep[String] = column[String]("LOGIN_MEMBER_STATUS_CODE", O.Length(3,varying=false))

    /** Foreign key referencing Member (database name FK_MEMBER_LOGIN_MEMBER) */
    lazy val memberFk = foreignKey("FK_MEMBER_LOGIN_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing MemberStatus (database name FK_MEMBER_LOGIN_MEMBER_STATUS) */
    lazy val memberStatusFk = foreignKey("FK_MEMBER_LOGIN_MEMBER_STATUS", loginMemberStatusCode, MemberStatus)(r => r.memberStatusCode, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (loginDatetime) (database name IX_MEMBER_LOGIN_DATETIME) */
    val index1 = index("IX_MEMBER_LOGIN_DATETIME", loginDatetime)
    /** Uniqueness Index over (memberId,loginDatetime) (database name MEMBER_ID) */
    val index2 = index("MEMBER_ID", (memberId, loginDatetime), unique=true)
  }
  /** Collection-like TableQuery object for table MemberLogin */
  lazy val MemberLogin = new TableQuery(tag => new MemberLogin(tag))

  /** Entity class storing rows of table MemberSecurity
   *  @param memberId Database column MEMBER_ID SqlType(INT), PrimaryKey
   *  @param loginPassword Database column LOGIN_PASSWORD SqlType(VARCHAR), Length(50,true)
   *  @param reminderQuestion Database column REMINDER_QUESTION SqlType(VARCHAR), Length(50,true)
   *  @param reminderAnswer Database column REMINDER_ANSWER SqlType(VARCHAR), Length(50,true)
   *  @param reminderUseCount Database column REMINDER_USE_COUNT SqlType(INT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberSecurityRow(memberId: Int, loginPassword: String, reminderQuestion: String, reminderAnswer: String, reminderUseCount: Int, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching MemberSecurityRow objects using plain SQL queries */
  implicit def GetResultMemberSecurityRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Long]): GR[MemberSecurityRow] = GR{
    prs => import prs._
    MemberSecurityRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table MEMBER_SECURITY. Objects of this class serve as prototypes for rows in queries. */
  class MemberSecurity(_tableTag: Tag) extends Table[MemberSecurityRow](_tableTag, "MEMBER_SECURITY") {
    def * = (memberId, loginPassword, reminderQuestion, reminderAnswer, reminderUseCount, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (MemberSecurityRow.tupled, MemberSecurityRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(loginPassword), Rep.Some(reminderQuestion), Rep.Some(reminderAnswer), Rep.Some(reminderUseCount), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberSecurityRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ID SqlType(INT), PrimaryKey */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID", O.PrimaryKey)
    /** Database column LOGIN_PASSWORD SqlType(VARCHAR), Length(50,true) */
    val loginPassword: Rep[String] = column[String]("LOGIN_PASSWORD", O.Length(50,varying=true))
    /** Database column REMINDER_QUESTION SqlType(VARCHAR), Length(50,true) */
    val reminderQuestion: Rep[String] = column[String]("REMINDER_QUESTION", O.Length(50,varying=true))
    /** Database column REMINDER_ANSWER SqlType(VARCHAR), Length(50,true) */
    val reminderAnswer: Rep[String] = column[String]("REMINDER_ANSWER", O.Length(50,varying=true))
    /** Database column REMINDER_USE_COUNT SqlType(INT) */
    val reminderUseCount: Rep[Int] = column[Int]("REMINDER_USE_COUNT")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_MEMBER_SECURITY_MEMBER) */
    lazy val memberFk = foreignKey("FK_MEMBER_SECURITY_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table MemberSecurity */
  lazy val MemberSecurity = new TableQuery(tag => new MemberSecurity(tag))

  /** Entity class storing rows of table MemberService
   *  @param memberServiceId Database column MEMBER_SERVICE_ID SqlType(INT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(INT)
   *  @param servicePointCount Database column SERVICE_POINT_COUNT SqlType(INT)
   *  @param serviceRankCode Database column SERVICE_RANK_CODE SqlType(CHAR), Length(3,false)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberServiceRow(memberServiceId: Int, memberId: Int, servicePointCount: Int, serviceRankCode: String, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching MemberServiceRow objects using plain SQL queries */
  implicit def GetResultMemberServiceRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Long]): GR[MemberServiceRow] = GR{
    prs => import prs._
    MemberServiceRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table MEMBER_SERVICE. Objects of this class serve as prototypes for rows in queries. */
  class MemberService(_tableTag: Tag) extends Table[MemberServiceRow](_tableTag, "MEMBER_SERVICE") {
    def * = (memberServiceId, memberId, servicePointCount, serviceRankCode, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (MemberServiceRow.tupled, MemberServiceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberServiceId), Rep.Some(memberId), Rep.Some(servicePointCount), Rep.Some(serviceRankCode), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberServiceRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_SERVICE_ID SqlType(INT), AutoInc, PrimaryKey */
    val memberServiceId: Rep[Int] = column[Int]("MEMBER_SERVICE_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(INT) */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID")
    /** Database column SERVICE_POINT_COUNT SqlType(INT) */
    val servicePointCount: Rep[Int] = column[Int]("SERVICE_POINT_COUNT")
    /** Database column SERVICE_RANK_CODE SqlType(CHAR), Length(3,false) */
    val serviceRankCode: Rep[String] = column[String]("SERVICE_RANK_CODE", O.Length(3,varying=false))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_MEMBER_SERVICE_MEMBER) */
    lazy val memberFk = foreignKey("FK_MEMBER_SERVICE_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing ServiceRank (database name FK_MEMBER_SERVICE_SERVICE_RANK_CODE) */
    lazy val serviceRankFk = foreignKey("FK_MEMBER_SERVICE_SERVICE_RANK_CODE", serviceRankCode, ServiceRank)(r => r.serviceRankCode, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (servicePointCount) (database name IX_MEMBER_SERVICE_POINT_COUNT) */
    val index1 = index("IX_MEMBER_SERVICE_POINT_COUNT", servicePointCount)
    /** Uniqueness Index over (memberId) (database name MEMBER_ID) */
    val index2 = index("MEMBER_ID", memberId, unique=true)
  }
  /** Collection-like TableQuery object for table MemberService */
  lazy val MemberService = new TableQuery(tag => new MemberService(tag))

  /** Entity class storing rows of table MemberStatus
   *  @param memberStatusCode Database column MEMBER_STATUS_CODE SqlType(CHAR), PrimaryKey, Length(3,false)
   *  @param memberStatusName Database column MEMBER_STATUS_NAME SqlType(VARCHAR), Length(50,true)
   *  @param description Database column DESCRIPTION SqlType(VARCHAR), Length(200,true)
   *  @param displayOrder Database column DISPLAY_ORDER SqlType(INT) */
  case class MemberStatusRow(memberStatusCode: String, memberStatusName: String, description: String, displayOrder: Int)
  /** GetResult implicit for fetching MemberStatusRow objects using plain SQL queries */
  implicit def GetResultMemberStatusRow(implicit e0: GR[String], e1: GR[Int]): GR[MemberStatusRow] = GR{
    prs => import prs._
    MemberStatusRow.tupled((<<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table MEMBER_STATUS. Objects of this class serve as prototypes for rows in queries. */
  class MemberStatus(_tableTag: Tag) extends Table[MemberStatusRow](_tableTag, "MEMBER_STATUS") {
    def * = (memberStatusCode, memberStatusName, description, displayOrder) <> (MemberStatusRow.tupled, MemberStatusRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberStatusCode), Rep.Some(memberStatusName), Rep.Some(description), Rep.Some(displayOrder)).shaped.<>({r=>import r._; _1.map(_=> MemberStatusRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_STATUS_CODE SqlType(CHAR), PrimaryKey, Length(3,false) */
    val memberStatusCode: Rep[String] = column[String]("MEMBER_STATUS_CODE", O.PrimaryKey, O.Length(3,varying=false))
    /** Database column MEMBER_STATUS_NAME SqlType(VARCHAR), Length(50,true) */
    val memberStatusName: Rep[String] = column[String]("MEMBER_STATUS_NAME", O.Length(50,varying=true))
    /** Database column DESCRIPTION SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("DESCRIPTION", O.Length(200,varying=true))
    /** Database column DISPLAY_ORDER SqlType(INT) */
    val displayOrder: Rep[Int] = column[Int]("DISPLAY_ORDER")

    /** Uniqueness Index over (displayOrder) (database name DISPLAY_ORDER) */
    val index1 = index("DISPLAY_ORDER", displayOrder, unique=true)
  }
  /** Collection-like TableQuery object for table MemberStatus */
  lazy val MemberStatus = new TableQuery(tag => new MemberStatus(tag))

  /** Entity class storing rows of table MemberWithdrawal
   *  @param memberId Database column MEMBER_ID SqlType(INT), PrimaryKey
   *  @param withdrawalReasonCode Database column WITHDRAWAL_REASON_CODE SqlType(CHAR), Length(3,false), Default(None)
   *  @param withdrawalReasonInputText Database column WITHDRAWAL_REASON_INPUT_TEXT SqlType(TEXT), Default(None)
   *  @param withdrawalDatetime Database column WITHDRAWAL_DATETIME SqlType(DATETIME)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
  case class MemberWithdrawalRow(memberId: Int, withdrawalReasonCode: Option[String] = None, withdrawalReasonInputText: Option[String] = None, withdrawalDatetime: java.sql.Timestamp, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String)
  /** GetResult implicit for fetching MemberWithdrawalRow objects using plain SQL queries */
  implicit def GetResultMemberWithdrawalRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[java.sql.Timestamp], e3: GR[String]): GR[MemberWithdrawalRow] = GR{
    prs => import prs._
    MemberWithdrawalRow.tupled((<<[Int], <<?[String], <<?[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table MEMBER_WITHDRAWAL. Objects of this class serve as prototypes for rows in queries. */
  class MemberWithdrawal(_tableTag: Tag) extends Table[MemberWithdrawalRow](_tableTag, "MEMBER_WITHDRAWAL") {
    def * = (memberId, withdrawalReasonCode, withdrawalReasonInputText, withdrawalDatetime, registerDatetime, registerUser, updateDatetime, updateUser) <> (MemberWithdrawalRow.tupled, MemberWithdrawalRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), withdrawalReasonCode, withdrawalReasonInputText, Rep.Some(withdrawalDatetime), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser)).shaped.<>({r=>import r._; _1.map(_=> MemberWithdrawalRow.tupled((_1.get, _2, _3, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ID SqlType(INT), PrimaryKey */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID", O.PrimaryKey)
    /** Database column WITHDRAWAL_REASON_CODE SqlType(CHAR), Length(3,false), Default(None) */
    val withdrawalReasonCode: Rep[Option[String]] = column[Option[String]]("WITHDRAWAL_REASON_CODE", O.Length(3,varying=false), O.Default(None))
    /** Database column WITHDRAWAL_REASON_INPUT_TEXT SqlType(TEXT), Default(None) */
    val withdrawalReasonInputText: Rep[Option[String]] = column[Option[String]]("WITHDRAWAL_REASON_INPUT_TEXT", O.Default(None))
    /** Database column WITHDRAWAL_DATETIME SqlType(DATETIME) */
    val withdrawalDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("WITHDRAWAL_DATETIME")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))

    /** Foreign key referencing Member (database name FK_MEMBER_WITHDRAWAL_MEMBER) */
    lazy val memberFk = foreignKey("FK_MEMBER_WITHDRAWAL_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing WithdrawalReason (database name FK_MEMBER_WITHDRAWAL_WITHDRAWAL_REASON) */
    lazy val withdrawalReasonFk = foreignKey("FK_MEMBER_WITHDRAWAL_WITHDRAWAL_REASON", withdrawalReasonCode, WithdrawalReason)(r => Rep.Some(r.withdrawalReasonCode), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table MemberWithdrawal */
  lazy val MemberWithdrawal = new TableQuery(tag => new MemberWithdrawal(tag))

  /** Entity class storing rows of table Product
   *  @param productId Database column PRODUCT_ID SqlType(INT), AutoInc, PrimaryKey
   *  @param productName Database column PRODUCT_NAME SqlType(VARCHAR), Length(50,true)
   *  @param productHandleCode Database column PRODUCT_HANDLE_CODE SqlType(VARCHAR), Length(100,true)
   *  @param productCategoryCode Database column PRODUCT_CATEGORY_CODE SqlType(CHAR), Length(3,false)
   *  @param productStatusCode Database column PRODUCT_STATUS_CODE SqlType(CHAR), Length(3,false)
   *  @param regularPrice Database column REGULAR_PRICE SqlType(INT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class ProductRow(productId: Int, productName: String, productHandleCode: String, productCategoryCode: String, productStatusCode: String, regularPrice: Int, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching ProductRow objects using plain SQL queries */
  implicit def GetResultProductRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Long]): GR[ProductRow] = GR{
    prs => import prs._
    ProductRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table PRODUCT. Objects of this class serve as prototypes for rows in queries. */
  class Product(_tableTag: Tag) extends Table[ProductRow](_tableTag, "PRODUCT") {
    def * = (productId, productName, productHandleCode, productCategoryCode, productStatusCode, regularPrice, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (ProductRow.tupled, ProductRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(productId), Rep.Some(productName), Rep.Some(productHandleCode), Rep.Some(productCategoryCode), Rep.Some(productStatusCode), Rep.Some(regularPrice), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> ProductRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PRODUCT_ID SqlType(INT), AutoInc, PrimaryKey */
    val productId: Rep[Int] = column[Int]("PRODUCT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column PRODUCT_NAME SqlType(VARCHAR), Length(50,true) */
    val productName: Rep[String] = column[String]("PRODUCT_NAME", O.Length(50,varying=true))
    /** Database column PRODUCT_HANDLE_CODE SqlType(VARCHAR), Length(100,true) */
    val productHandleCode: Rep[String] = column[String]("PRODUCT_HANDLE_CODE", O.Length(100,varying=true))
    /** Database column PRODUCT_CATEGORY_CODE SqlType(CHAR), Length(3,false) */
    val productCategoryCode: Rep[String] = column[String]("PRODUCT_CATEGORY_CODE", O.Length(3,varying=false))
    /** Database column PRODUCT_STATUS_CODE SqlType(CHAR), Length(3,false) */
    val productStatusCode: Rep[String] = column[String]("PRODUCT_STATUS_CODE", O.Length(3,varying=false))
    /** Database column REGULAR_PRICE SqlType(INT) */
    val regularPrice: Rep[Int] = column[Int]("REGULAR_PRICE")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing ProductCategory (database name FK_PRODUCT_PRODUCT_CATEGORY) */
    lazy val productCategoryFk = foreignKey("FK_PRODUCT_PRODUCT_CATEGORY", productCategoryCode, ProductCategory)(r => r.productCategoryCode, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing ProductStatus (database name FK_PRODUCT_PRODUCT_STATUS) */
    lazy val productStatusFk = foreignKey("FK_PRODUCT_PRODUCT_STATUS", productStatusCode, ProductStatus)(r => r.productStatusCode, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (productName) (database name IX_PRODUCT_PRODUCT_NAME) */
    val index1 = index("IX_PRODUCT_PRODUCT_NAME", productName)
    /** Uniqueness Index over (productHandleCode) (database name PRODUCT_HANDLE_CODE) */
    val index2 = index("PRODUCT_HANDLE_CODE", productHandleCode, unique=true)
  }
  /** Collection-like TableQuery object for table Product */
  lazy val Product = new TableQuery(tag => new Product(tag))

  /** Entity class storing rows of table ProductCategory
   *  @param productCategoryCode Database column PRODUCT_CATEGORY_CODE SqlType(CHAR), PrimaryKey, Length(3,false)
   *  @param productCategoryName Database column PRODUCT_CATEGORY_NAME SqlType(VARCHAR), Length(50,true)
   *  @param parentCategoryCode Database column PARENT_CATEGORY_CODE SqlType(CHAR), Length(3,false), Default(None) */
  case class ProductCategoryRow(productCategoryCode: String, productCategoryName: String, parentCategoryCode: Option[String] = None)
  /** GetResult implicit for fetching ProductCategoryRow objects using plain SQL queries */
  implicit def GetResultProductCategoryRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[ProductCategoryRow] = GR{
    prs => import prs._
    ProductCategoryRow.tupled((<<[String], <<[String], <<?[String]))
  }
  /** Table description of table PRODUCT_CATEGORY. Objects of this class serve as prototypes for rows in queries. */
  class ProductCategory(_tableTag: Tag) extends Table[ProductCategoryRow](_tableTag, "PRODUCT_CATEGORY") {
    def * = (productCategoryCode, productCategoryName, parentCategoryCode) <> (ProductCategoryRow.tupled, ProductCategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(productCategoryCode), Rep.Some(productCategoryName), parentCategoryCode).shaped.<>({r=>import r._; _1.map(_=> ProductCategoryRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PRODUCT_CATEGORY_CODE SqlType(CHAR), PrimaryKey, Length(3,false) */
    val productCategoryCode: Rep[String] = column[String]("PRODUCT_CATEGORY_CODE", O.PrimaryKey, O.Length(3,varying=false))
    /** Database column PRODUCT_CATEGORY_NAME SqlType(VARCHAR), Length(50,true) */
    val productCategoryName: Rep[String] = column[String]("PRODUCT_CATEGORY_NAME", O.Length(50,varying=true))
    /** Database column PARENT_CATEGORY_CODE SqlType(CHAR), Length(3,false), Default(None) */
    val parentCategoryCode: Rep[Option[String]] = column[Option[String]]("PARENT_CATEGORY_CODE", O.Length(3,varying=false), O.Default(None))

    /** Foreign key referencing ProductCategory (database name FK_PRODUCT_CATEGORY_PARENT) */
    lazy val productCategoryFk = foreignKey("FK_PRODUCT_CATEGORY_PARENT", parentCategoryCode, ProductCategory)(r => Rep.Some(r.productCategoryCode), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ProductCategory */
  lazy val ProductCategory = new TableQuery(tag => new ProductCategory(tag))

  /** Entity class storing rows of table ProductStatus
   *  @param productStatusCode Database column PRODUCT_STATUS_CODE SqlType(CHAR), PrimaryKey, Length(3,false)
   *  @param productStatusName Database column PRODUCT_STATUS_NAME SqlType(VARCHAR), Length(50,true)
   *  @param displayOrder Database column DISPLAY_ORDER SqlType(INT) */
  case class ProductStatusRow(productStatusCode: String, productStatusName: String, displayOrder: Int)
  /** GetResult implicit for fetching ProductStatusRow objects using plain SQL queries */
  implicit def GetResultProductStatusRow(implicit e0: GR[String], e1: GR[Int]): GR[ProductStatusRow] = GR{
    prs => import prs._
    ProductStatusRow.tupled((<<[String], <<[String], <<[Int]))
  }
  /** Table description of table PRODUCT_STATUS. Objects of this class serve as prototypes for rows in queries. */
  class ProductStatus(_tableTag: Tag) extends Table[ProductStatusRow](_tableTag, "PRODUCT_STATUS") {
    def * = (productStatusCode, productStatusName, displayOrder) <> (ProductStatusRow.tupled, ProductStatusRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(productStatusCode), Rep.Some(productStatusName), Rep.Some(displayOrder)).shaped.<>({r=>import r._; _1.map(_=> ProductStatusRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PRODUCT_STATUS_CODE SqlType(CHAR), PrimaryKey, Length(3,false) */
    val productStatusCode: Rep[String] = column[String]("PRODUCT_STATUS_CODE", O.PrimaryKey, O.Length(3,varying=false))
    /** Database column PRODUCT_STATUS_NAME SqlType(VARCHAR), Length(50,true) */
    val productStatusName: Rep[String] = column[String]("PRODUCT_STATUS_NAME", O.Length(50,varying=true))
    /** Database column DISPLAY_ORDER SqlType(INT) */
    val displayOrder: Rep[Int] = column[Int]("DISPLAY_ORDER")

    /** Uniqueness Index over (displayOrder) (database name DISPLAY_ORDER) */
    val index1 = index("DISPLAY_ORDER", displayOrder, unique=true)
  }
  /** Collection-like TableQuery object for table ProductStatus */
  lazy val ProductStatus = new TableQuery(tag => new ProductStatus(tag))

  /** Entity class storing rows of table Purchase
   *  @param purchaseId Database column PURCHASE_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(INT)
   *  @param productId Database column PRODUCT_ID SqlType(INT)
   *  @param purchaseDatetime Database column PURCHASE_DATETIME SqlType(DATETIME)
   *  @param purchaseCount Database column PURCHASE_COUNT SqlType(INT)
   *  @param purchasePrice Database column PURCHASE_PRICE SqlType(INT)
   *  @param paymentCompleteFlg Database column PAYMENT_COMPLETE_FLG SqlType(INT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class PurchaseRow(purchaseId: Long, memberId: Int, productId: Int, purchaseDatetime: java.sql.Timestamp, purchaseCount: Int, purchasePrice: Int, paymentCompleteFlg: Int, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching PurchaseRow objects using plain SQL queries */
  implicit def GetResultPurchaseRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[java.sql.Timestamp], e3: GR[String]): GR[PurchaseRow] = GR{
    prs => import prs._
    PurchaseRow.tupled((<<[Long], <<[Int], <<[Int], <<[java.sql.Timestamp], <<[Int], <<[Int], <<[Int], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table PURCHASE. Objects of this class serve as prototypes for rows in queries. */
  class Purchase(_tableTag: Tag) extends Table[PurchaseRow](_tableTag, "PURCHASE") {
    def * = (purchaseId, memberId, productId, purchaseDatetime, purchaseCount, purchasePrice, paymentCompleteFlg, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (PurchaseRow.tupled, PurchaseRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(purchaseId), Rep.Some(memberId), Rep.Some(productId), Rep.Some(purchaseDatetime), Rep.Some(purchaseCount), Rep.Some(purchasePrice), Rep.Some(paymentCompleteFlg), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> PurchaseRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PURCHASE_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val purchaseId: Rep[Long] = column[Long]("PURCHASE_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(INT) */
    val memberId: Rep[Int] = column[Int]("MEMBER_ID")
    /** Database column PRODUCT_ID SqlType(INT) */
    val productId: Rep[Int] = column[Int]("PRODUCT_ID")
    /** Database column PURCHASE_DATETIME SqlType(DATETIME) */
    val purchaseDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("PURCHASE_DATETIME")
    /** Database column PURCHASE_COUNT SqlType(INT) */
    val purchaseCount: Rep[Int] = column[Int]("PURCHASE_COUNT")
    /** Database column PURCHASE_PRICE SqlType(INT) */
    val purchasePrice: Rep[Int] = column[Int]("PURCHASE_PRICE")
    /** Database column PAYMENT_COMPLETE_FLG SqlType(INT) */
    val paymentCompleteFlg: Rep[Int] = column[Int]("PAYMENT_COMPLETE_FLG")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_PURCHASE_MEMBER) */
    lazy val memberFk = foreignKey("FK_PURCHASE_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Product (database name FK_PURCHASE_PRODUCT) */
    lazy val productFk = foreignKey("FK_PURCHASE_PRODUCT", productId, Product)(r => r.productId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (purchaseDatetime,memberId) (database name IX_PURCHASE_DATETIME_MEMBER) */
    val index1 = index("IX_PURCHASE_DATETIME_MEMBER", (purchaseDatetime, memberId))
    /** Index over (purchasePrice) (database name IX_PURCHASE_PRICE) */
    val index2 = index("IX_PURCHASE_PRICE", purchasePrice)
    /** Index over (productId,purchaseDatetime) (database name IX_PURCHASE_PRODUCT_DATETIME) */
    val index3 = index("IX_PURCHASE_PRODUCT_DATETIME", (productId, purchaseDatetime))
    /** Uniqueness Index over (memberId,productId,purchaseDatetime) (database name MEMBER_ID) */
    val index4 = index("MEMBER_ID", (memberId, productId, purchaseDatetime), unique=true)
  }
  /** Collection-like TableQuery object for table Purchase */
  lazy val Purchase = new TableQuery(tag => new Purchase(tag))

  /** Entity class storing rows of table PurchasePayment
   *  @param purchasePaymentId Database column PURCHASE_PAYMENT_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param purchaseId Database column PURCHASE_ID SqlType(BIGINT)
   *  @param paymentAmount Database column PAYMENT_AMOUNT SqlType(DECIMAL)
   *  @param paymentDatetime Database column PAYMENT_DATETIME SqlType(DATETIME)
   *  @param paymentMethodCode Database column PAYMENT_METHOD_CODE SqlType(CHAR), Length(3,false)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
  case class PurchasePaymentRow(purchasePaymentId: Long, purchaseId: Long, paymentAmount: scala.math.BigDecimal, paymentDatetime: java.sql.Timestamp, paymentMethodCode: String, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String)
  /** GetResult implicit for fetching PurchasePaymentRow objects using plain SQL queries */
  implicit def GetResultPurchasePaymentRow(implicit e0: GR[Long], e1: GR[scala.math.BigDecimal], e2: GR[java.sql.Timestamp], e3: GR[String]): GR[PurchasePaymentRow] = GR{
    prs => import prs._
    PurchasePaymentRow.tupled((<<[Long], <<[Long], <<[scala.math.BigDecimal], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table PURCHASE_PAYMENT. Objects of this class serve as prototypes for rows in queries. */
  class PurchasePayment(_tableTag: Tag) extends Table[PurchasePaymentRow](_tableTag, "PURCHASE_PAYMENT") {
    def * = (purchasePaymentId, purchaseId, paymentAmount, paymentDatetime, paymentMethodCode, registerDatetime, registerUser, updateDatetime, updateUser) <> (PurchasePaymentRow.tupled, PurchasePaymentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(purchasePaymentId), Rep.Some(purchaseId), Rep.Some(paymentAmount), Rep.Some(paymentDatetime), Rep.Some(paymentMethodCode), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser)).shaped.<>({r=>import r._; _1.map(_=> PurchasePaymentRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PURCHASE_PAYMENT_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val purchasePaymentId: Rep[Long] = column[Long]("PURCHASE_PAYMENT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column PURCHASE_ID SqlType(BIGINT) */
    val purchaseId: Rep[Long] = column[Long]("PURCHASE_ID")
    /** Database column PAYMENT_AMOUNT SqlType(DECIMAL) */
    val paymentAmount: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("PAYMENT_AMOUNT")
    /** Database column PAYMENT_DATETIME SqlType(DATETIME) */
    val paymentDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("PAYMENT_DATETIME")
    /** Database column PAYMENT_METHOD_CODE SqlType(CHAR), Length(3,false) */
    val paymentMethodCode: Rep[String] = column[String]("PAYMENT_METHOD_CODE", O.Length(3,varying=false))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column REGISTER_USER SqlType(VARCHAR), Length(200,true) */
    val registerUser: Rep[String] = column[String]("REGISTER_USER", O.Length(200,varying=true))
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column UPDATE_USER SqlType(VARCHAR), Length(200,true) */
    val updateUser: Rep[String] = column[String]("UPDATE_USER", O.Length(200,varying=true))

    /** Foreign key referencing Purchase (database name FK_PURCHASE_PAYMENT_PURCHASE) */
    lazy val purchaseFk = foreignKey("FK_PURCHASE_PAYMENT_PURCHASE", purchaseId, Purchase)(r => r.purchaseId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (paymentDatetime,paymentAmount) (database name IX_PURCHASE_PAYMENT_DATETIME) */
    val index1 = index("IX_PURCHASE_PAYMENT_DATETIME", (paymentDatetime, paymentAmount))
  }
  /** Collection-like TableQuery object for table PurchasePayment */
  lazy val PurchasePayment = new TableQuery(tag => new PurchasePayment(tag))

  /** Entity class storing rows of table Region
   *  @param regionId Database column REGION_ID SqlType(INT), PrimaryKey
   *  @param regionName Database column REGION_NAME SqlType(VARCHAR), Length(50,true) */
  case class RegionRow(regionId: Int, regionName: String)
  /** GetResult implicit for fetching RegionRow objects using plain SQL queries */
  implicit def GetResultRegionRow(implicit e0: GR[Int], e1: GR[String]): GR[RegionRow] = GR{
    prs => import prs._
    RegionRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table REGION. Objects of this class serve as prototypes for rows in queries. */
  class Region(_tableTag: Tag) extends Table[RegionRow](_tableTag, "REGION") {
    def * = (regionId, regionName) <> (RegionRow.tupled, RegionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(regionId), Rep.Some(regionName)).shaped.<>({r=>import r._; _1.map(_=> RegionRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column REGION_ID SqlType(INT), PrimaryKey */
    val regionId: Rep[Int] = column[Int]("REGION_ID", O.PrimaryKey)
    /** Database column REGION_NAME SqlType(VARCHAR), Length(50,true) */
    val regionName: Rep[String] = column[String]("REGION_NAME", O.Length(50,varying=true))
  }
  /** Collection-like TableQuery object for table Region */
  lazy val Region = new TableQuery(tag => new Region(tag))

  /** Entity class storing rows of table ServiceRank
   *  @param serviceRankCode Database column SERVICE_RANK_CODE SqlType(CHAR), PrimaryKey, Length(3,false)
   *  @param serviceRankName Database column SERVICE_RANK_NAME SqlType(VARCHAR), Length(50,true)
   *  @param servicePointIncidence Database column SERVICE_POINT_INCIDENCE SqlType(DECIMAL)
   *  @param newAcceptableFlg Database column NEW_ACCEPTABLE_FLG SqlType(INT)
   *  @param description Database column DESCRIPTION SqlType(VARCHAR), Length(200,true)
   *  @param displayOrder Database column DISPLAY_ORDER SqlType(INT) */
  case class ServiceRankRow(serviceRankCode: String, serviceRankName: String, servicePointIncidence: scala.math.BigDecimal, newAcceptableFlg: Int, description: String, displayOrder: Int)
  /** GetResult implicit for fetching ServiceRankRow objects using plain SQL queries */
  implicit def GetResultServiceRankRow(implicit e0: GR[String], e1: GR[scala.math.BigDecimal], e2: GR[Int]): GR[ServiceRankRow] = GR{
    prs => import prs._
    ServiceRankRow.tupled((<<[String], <<[String], <<[scala.math.BigDecimal], <<[Int], <<[String], <<[Int]))
  }
  /** Table description of table SERVICE_RANK. Objects of this class serve as prototypes for rows in queries. */
  class ServiceRank(_tableTag: Tag) extends Table[ServiceRankRow](_tableTag, "SERVICE_RANK") {
    def * = (serviceRankCode, serviceRankName, servicePointIncidence, newAcceptableFlg, description, displayOrder) <> (ServiceRankRow.tupled, ServiceRankRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(serviceRankCode), Rep.Some(serviceRankName), Rep.Some(servicePointIncidence), Rep.Some(newAcceptableFlg), Rep.Some(description), Rep.Some(displayOrder)).shaped.<>({r=>import r._; _1.map(_=> ServiceRankRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column SERVICE_RANK_CODE SqlType(CHAR), PrimaryKey, Length(3,false) */
    val serviceRankCode: Rep[String] = column[String]("SERVICE_RANK_CODE", O.PrimaryKey, O.Length(3,varying=false))
    /** Database column SERVICE_RANK_NAME SqlType(VARCHAR), Length(50,true) */
    val serviceRankName: Rep[String] = column[String]("SERVICE_RANK_NAME", O.Length(50,varying=true))
    /** Database column SERVICE_POINT_INCIDENCE SqlType(DECIMAL) */
    val servicePointIncidence: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("SERVICE_POINT_INCIDENCE")
    /** Database column NEW_ACCEPTABLE_FLG SqlType(INT) */
    val newAcceptableFlg: Rep[Int] = column[Int]("NEW_ACCEPTABLE_FLG")
    /** Database column DESCRIPTION SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("DESCRIPTION", O.Length(200,varying=true))
    /** Database column DISPLAY_ORDER SqlType(INT) */
    val displayOrder: Rep[Int] = column[Int]("DISPLAY_ORDER")

    /** Uniqueness Index over (displayOrder) (database name DISPLAY_ORDER) */
    val index1 = index("DISPLAY_ORDER", displayOrder, unique=true)
  }
  /** Collection-like TableQuery object for table ServiceRank */
  lazy val ServiceRank = new TableQuery(tag => new ServiceRank(tag))

  /** Entity class storing rows of table WithdrawalReason
   *  @param withdrawalReasonCode Database column WITHDRAWAL_REASON_CODE SqlType(CHAR), PrimaryKey, Length(3,false)
   *  @param withdrawalReasonText Database column WITHDRAWAL_REASON_TEXT SqlType(TEXT)
   *  @param displayOrder Database column DISPLAY_ORDER SqlType(INT) */
  case class WithdrawalReasonRow(withdrawalReasonCode: String, withdrawalReasonText: String, displayOrder: Int)
  /** GetResult implicit for fetching WithdrawalReasonRow objects using plain SQL queries */
  implicit def GetResultWithdrawalReasonRow(implicit e0: GR[String], e1: GR[Int]): GR[WithdrawalReasonRow] = GR{
    prs => import prs._
    WithdrawalReasonRow.tupled((<<[String], <<[String], <<[Int]))
  }
  /** Table description of table WITHDRAWAL_REASON. Objects of this class serve as prototypes for rows in queries. */
  class WithdrawalReason(_tableTag: Tag) extends Table[WithdrawalReasonRow](_tableTag, "WITHDRAWAL_REASON") {
    def * = (withdrawalReasonCode, withdrawalReasonText, displayOrder) <> (WithdrawalReasonRow.tupled, WithdrawalReasonRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(withdrawalReasonCode), Rep.Some(withdrawalReasonText), Rep.Some(displayOrder)).shaped.<>({r=>import r._; _1.map(_=> WithdrawalReasonRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column WITHDRAWAL_REASON_CODE SqlType(CHAR), PrimaryKey, Length(3,false) */
    val withdrawalReasonCode: Rep[String] = column[String]("WITHDRAWAL_REASON_CODE", O.PrimaryKey, O.Length(3,varying=false))
    /** Database column WITHDRAWAL_REASON_TEXT SqlType(TEXT) */
    val withdrawalReasonText: Rep[String] = column[String]("WITHDRAWAL_REASON_TEXT")
    /** Database column DISPLAY_ORDER SqlType(INT) */
    val displayOrder: Rep[Int] = column[Int]("DISPLAY_ORDER")

    /** Uniqueness Index over (displayOrder) (database name DISPLAY_ORDER) */
    val index1 = index("DISPLAY_ORDER", displayOrder, unique=true)
  }
  /** Collection-like TableQuery object for table WithdrawalReason */
  lazy val WithdrawalReason = new TableQuery(tag => new WithdrawalReason(tag))
}
