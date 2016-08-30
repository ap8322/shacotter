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
  lazy val schema: profile.SchemaDescription = Array(Follow.schema, Image.schema, Member.schema, SchemaVersion.schema, Tweet.schema, TweetEvaluate.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Follow
   *  @param followerId Database column FOLLOWER_ID SqlType(BIGINT)
   *  @param followedId Database column FOLLOWED_ID SqlType(BIGINT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class FollowRow(followerId: Long, followedId: Long, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching FollowRow objects using plain SQL queries */
  implicit def GetResultFollowRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[String]): GR[FollowRow] = GR{
    prs => import prs._
    FollowRow.tupled((<<[Long], <<[Long], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table FOLLOW. Objects of this class serve as prototypes for rows in queries. */
  class Follow(_tableTag: Tag) extends Table[FollowRow](_tableTag, "FOLLOW") {
    def * = (followerId, followedId, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (FollowRow.tupled, FollowRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(followerId), Rep.Some(followedId), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> FollowRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column FOLLOWER_ID SqlType(BIGINT) */
    val followerId: Rep[Long] = column[Long]("FOLLOWER_ID")
    /** Database column FOLLOWED_ID SqlType(BIGINT) */
    val followedId: Rep[Long] = column[Long]("FOLLOWED_ID")
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

    /** Foreign key referencing Member (database name follow_ibfk_1) */
    lazy val memberFk1 = foreignKey("follow_ibfk_1", followedId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Member (database name follow_ibfk_2) */
    lazy val memberFk2 = foreignKey("follow_ibfk_2", followerId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (followerId,followedId) (database name FOLLOWER_ID) */
    val index1 = index("FOLLOWER_ID", (followerId, followedId), unique=true)
  }
  /** Collection-like TableQuery object for table Follow */
  lazy val Follow = new TableQuery(tag => new Follow(tag))

  /** Entity class storing rows of table Image
   *  @param imageId Database column IMAGE_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT)
   *  @param imageName Database column IMAGE_NAME SqlType(VARCHAR), Length(200,true)
   *  @param imageData Database column IMAGE_DATA SqlType(MEDIUMBLOB)
   *  @param imageDataLength Database column IMAGE_DATA_LENGTH SqlType(BIGINT) */
  case class ImageRow(imageId: Long, memberId: Long, imageName: String, imageData: java.sql.Blob, imageDataLength: Long)
  /** GetResult implicit for fetching ImageRow objects using plain SQL queries */
  implicit def GetResultImageRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Blob]): GR[ImageRow] = GR{
    prs => import prs._
    ImageRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Blob], <<[Long]))
  }
  /** Table description of table IMAGE. Objects of this class serve as prototypes for rows in queries. */
  class Image(_tableTag: Tag) extends Table[ImageRow](_tableTag, "IMAGE") {
    def * = (imageId, memberId, imageName, imageData, imageDataLength) <> (ImageRow.tupled, ImageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(imageId), Rep.Some(memberId), Rep.Some(imageName), Rep.Some(imageData), Rep.Some(imageDataLength)).shaped.<>({r=>import r._; _1.map(_=> ImageRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column IMAGE_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val imageId: Rep[Long] = column[Long]("IMAGE_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(BIGINT) */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID")
    /** Database column IMAGE_NAME SqlType(VARCHAR), Length(200,true) */
    val imageName: Rep[String] = column[String]("IMAGE_NAME", O.Length(200,varying=true))
    /** Database column IMAGE_DATA SqlType(MEDIUMBLOB) */
    val imageData: Rep[java.sql.Blob] = column[java.sql.Blob]("IMAGE_DATA")
    /** Database column IMAGE_DATA_LENGTH SqlType(BIGINT) */
    val imageDataLength: Rep[Long] = column[Long]("IMAGE_DATA_LENGTH")

    /** Foreign key referencing Member (database name image_ibfk_1) */
    lazy val memberFk = foreignKey("image_ibfk_1", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Image */
  lazy val Image = new TableQuery(tag => new Image(tag))

  /** Entity class storing rows of table Member
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param email Database column EMAIL SqlType(VARCHAR), Length(200,true)
   *  @param password Database column PASSWORD SqlType(VARCHAR), Length(200,true)
   *  @param name Database column NAME SqlType(VARCHAR), Length(200,true)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberRow(memberId: Long, email: String, password: String, name: String, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table MEMBER. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends Table[MemberRow](_tableTag, "MEMBER") {
    def * = (memberId, email, password, name, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(email), Rep.Some(password), Rep.Some(name), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID", O.AutoInc, O.PrimaryKey)
    /** Database column EMAIL SqlType(VARCHAR), Length(200,true) */
    val email: Rep[String] = column[String]("EMAIL", O.Length(200,varying=true))
    /** Database column PASSWORD SqlType(VARCHAR), Length(200,true) */
    val password: Rep[String] = column[String]("PASSWORD", O.Length(200,varying=true))
    /** Database column NAME SqlType(VARCHAR), Length(200,true) */
    val name: Rep[String] = column[String]("NAME", O.Length(200,varying=true))
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

    /** Uniqueness Index over (email) (database name EMAIL) */
    val index1 = index("EMAIL", email, unique=true)
    /** Uniqueness Index over (email,name) (database name EMAIL_2) */
    val index2 = index("EMAIL_2", (email, name), unique=true)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table SchemaVersion
   *  @param installedRank Database column installed_rank SqlType(INT), PrimaryKey
   *  @param version Database column version SqlType(VARCHAR), Length(50,true), Default(None)
   *  @param description Database column description SqlType(VARCHAR), Length(200,true)
   *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
   *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
   *  @param checksum Database column checksum SqlType(INT), Default(None)
   *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
   *  @param executionTime Database column execution_time SqlType(INT)
   *  @param success Database column success SqlType(BIT) */
  case class SchemaVersionRow(installedRank: Int, version: Option[String] = None, description: String, `type`: String, script: String, checksum: Option[Int] = None, installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching SchemaVersionRow objects using plain SQL queries */
  implicit def GetResultSchemaVersionRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Int]], e4: GR[java.sql.Timestamp], e5: GR[Boolean]): GR[SchemaVersionRow] = GR{
    prs => import prs._
    SchemaVersionRow.tupled((<<[Int], <<?[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table schema_version. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class SchemaVersion(_tableTag: Tag) extends Table[SchemaVersionRow](_tableTag, "schema_version") {
    def * = (installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (SchemaVersionRow.tupled, SchemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(installedRank), version, Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> SchemaVersionRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column installed_rank SqlType(INT), PrimaryKey */
    val installedRank: Rep[Int] = column[Int]("installed_rank", O.PrimaryKey)
    /** Database column version SqlType(VARCHAR), Length(50,true), Default(None) */
    val version: Rep[Option[String]] = column[Option[String]]("version", O.Length(50,varying=true), O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(VARCHAR), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(INT), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))
    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(INT) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(BIT) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (success) (database name schema_version_s_idx) */
    val index1 = index("schema_version_s_idx", success)
  }
  /** Collection-like TableQuery object for table SchemaVersion */
  lazy val SchemaVersion = new TableQuery(tag => new SchemaVersion(tag))

  /** Entity class storing rows of table Tweet
   *  @param tweetId Database column TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT)
   *  @param tweet Database column TWEET SqlType(VARCHAR), Length(140,true)
   *  @param tweetAt Database column TWEET_AT SqlType(TIMESTAMP)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class TweetRow(tweetId: Long, memberId: Long, tweet: String, tweetAt: java.sql.Timestamp, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching TweetRow objects using plain SQL queries */
  implicit def GetResultTweetRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TweetRow] = GR{
    prs => import prs._
    TweetRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table TWEET. Objects of this class serve as prototypes for rows in queries. */
  class Tweet(_tableTag: Tag) extends Table[TweetRow](_tableTag, "TWEET") {
    def * = (tweetId, memberId, tweet, tweetAt, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (TweetRow.tupled, TweetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(tweetId), Rep.Some(memberId), Rep.Some(tweet), Rep.Some(tweetAt), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> TweetRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val tweetId: Rep[Long] = column[Long]("TWEET_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(BIGINT) */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID")
    /** Database column TWEET SqlType(VARCHAR), Length(140,true) */
    val tweet: Rep[String] = column[String]("TWEET", O.Length(140,varying=true))
    /** Database column TWEET_AT SqlType(TIMESTAMP) */
    val tweetAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("TWEET_AT")
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

    /** Foreign key referencing Member (database name tweet_ibfk_1) */
    lazy val memberFk = foreignKey("tweet_ibfk_1", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Tweet */
  lazy val Tweet = new TableQuery(tag => new Tweet(tag))

  /** Entity class storing rows of table TweetEvaluate
   *  @param tweetId Database column TWEET_ID SqlType(BIGINT)
   *  @param evaluateStatus Database column EVALUATE_STATUS SqlType(VARCHAR), Length(5,true)
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param registerUser Database column REGISTER_USER SqlType(VARCHAR), Length(200,true)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param updateUser Database column UPDATE_USER SqlType(VARCHAR), Length(200,true)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class TweetEvaluateRow(tweetId: Long, evaluateStatus: String, memberId: Long, registerDatetime: java.sql.Timestamp, registerUser: String, updateDatetime: java.sql.Timestamp, updateUser: String, versionNo: Long)
  /** GetResult implicit for fetching TweetEvaluateRow objects using plain SQL queries */
  implicit def GetResultTweetEvaluateRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TweetEvaluateRow] = GR{
    prs => import prs._
    TweetEvaluateRow.tupled((<<[Long], <<[String], <<[Long], <<[java.sql.Timestamp], <<[String], <<[java.sql.Timestamp], <<[String], <<[Long]))
  }
  /** Table description of table TWEET_EVALUATE. Objects of this class serve as prototypes for rows in queries. */
  class TweetEvaluate(_tableTag: Tag) extends Table[TweetEvaluateRow](_tableTag, "TWEET_EVALUATE") {
    def * = (tweetId, evaluateStatus, memberId, registerDatetime, registerUser, updateDatetime, updateUser, versionNo) <> (TweetEvaluateRow.tupled, TweetEvaluateRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(tweetId), Rep.Some(evaluateStatus), Rep.Some(memberId), Rep.Some(registerDatetime), Rep.Some(registerUser), Rep.Some(updateDatetime), Rep.Some(updateUser), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> TweetEvaluateRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column TWEET_ID SqlType(BIGINT) */
    val tweetId: Rep[Long] = column[Long]("TWEET_ID")
    /** Database column EVALUATE_STATUS SqlType(VARCHAR), Length(5,true) */
    val evaluateStatus: Rep[String] = column[String]("EVALUATE_STATUS", O.Length(5,varying=true))
    /** Database column MEMBER_ID SqlType(BIGINT) */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID")
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

    /** Foreign key referencing Member (database name tweet_evaluate_ibfk_2) */
    lazy val memberFk = foreignKey("tweet_evaluate_ibfk_2", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Tweet (database name tweet_evaluate_ibfk_1) */
    lazy val tweetFk = foreignKey("tweet_evaluate_ibfk_1", tweetId, Tweet)(r => r.tweetId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (tweetId,memberId) (database name TWEET_ID) */
    val index1 = index("TWEET_ID", (tweetId, memberId), unique=true)
  }
  /** Collection-like TableQuery object for table TweetEvaluate */
  lazy val TweetEvaluate = new TableQuery(tag => new TweetEvaluate(tag))
}
