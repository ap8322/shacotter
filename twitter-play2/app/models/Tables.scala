package models

import java.sql.Timestamp
import java.util.Date

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
  lazy val schema: profile.SchemaDescription = Follow.schema ++ Member.schema ++ PlayEvolutions.schema ++ Tweet.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Follow
   *  @param followerId Database column follower_id SqlType(INT)
   *  @param followedId Database column followed_id SqlType(INT) */
  case class FollowRow(followerId: Int, followedId: Int)
  /** GetResult implicit for fetching FollowRow objects using plain SQL queries */
  implicit def GetResultFollowRow(implicit e0: GR[Int]): GR[FollowRow] = GR{
    prs => import prs._
    FollowRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table Follow. Objects of this class serve as prototypes for rows in queries. */
  class Follow(_tableTag: Tag) extends Table[FollowRow](_tableTag, "Follow") {
    def * = (followerId, followedId) <> (FollowRow.tupled, FollowRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(followerId), Rep.Some(followedId)).shaped.<>({r=>import r._; _1.map(_=> FollowRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column follower_id SqlType(INT) */
    val followerId: Rep[Int] = column[Int]("follower_id")
    /** Database column followed_id SqlType(INT) */
    val followedId: Rep[Int] = column[Int]("followed_id")

    /** Foreign key referencing Member (database name follow_ibfk_1) */
    lazy val memberFk1 = foreignKey("follow_ibfk_1", followedId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Member (database name follow_ibfk_2) */
    lazy val memberFk2 = foreignKey("follow_ibfk_2", followerId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (followerId,followedId) (database name follower_id) */
    val index1 = index("follower_id", (followerId, followedId), unique=true)
  }
  /** Collection-like TableQuery object for table Follow */
  lazy val Follow = new TableQuery(tag => new Follow(tag))

  /** Entity class storing rows of table Member
   *  @param memberId Database column member_id SqlType(INT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(TEXT)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true) */
  case class MemberRow(memberId: Int, email: String, password: String, name: String)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Int], e1: GR[String]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table Member. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends Table[MemberRow](_tableTag, "Member") {
    def * = (memberId, email, password, name) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(email), Rep.Some(password), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(INT), AutoInc, PrimaryKey */
    val memberId: Rep[Int] = column[Int]("member_id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column password SqlType(TEXT) */
    val password: Rep[String] = column[String]("password")
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))

    /** Uniqueness Index over (email) (database name email) */
    val index1 = index("email", email, unique=true)
    /** Uniqueness Index over (email,name) (database name email_2) */
    val index2 = index("email_2", (email, name), unique=true)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param hash Database column hash SqlType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column applied_at SqlType(TIMESTAMP)
   *  @param applyScript Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param revertScript Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends Table[PlayEvolutionsRow](_tableTag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(VARCHAR), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(TIMESTAMP) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column state SqlType(VARCHAR), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Length(16777215,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Tweet
   *  @param tweetId Database column tweet_id SqlType(INT), AutoInc, PrimaryKey
   *  @param memberId Database column member_id SqlType(INT), Default(None)
   *  @param tweet Database column tweet SqlType(VARCHAR), Length(140,true), Default(None)
   *  @param tweetAt Database column tweet_at SqlType(TIMESTAMP) */
  case class TweetRow(tweetId: Int, memberId: Option[Int] = None, tweet: Option[String] = None, tweetAt: java.sql.Timestamp = new Timestamp(new Date().getTime))
  /** GetResult implicit for fetching TweetRow objects using plain SQL queries */
  implicit def GetResultTweetRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[TweetRow] = GR{
    prs => import prs._
    TweetRow.tupled((<<[Int], <<?[Int], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table Tweet. Objects of this class serve as prototypes for rows in queries. */
  class Tweet(_tableTag: Tag) extends Table[TweetRow](_tableTag, "Tweet") {
    def * = (tweetId, memberId, tweet, tweetAt) <> (TweetRow.tupled, TweetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(tweetId), memberId, tweet, Rep.Some(tweetAt)).shaped.<>({r=>import r._; _1.map(_=> TweetRow.tupled((_1.get, _2, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column tweet_id SqlType(INT), AutoInc, PrimaryKey */
    val tweetId: Rep[Int] = column[Int]("tweet_id", O.AutoInc, O.PrimaryKey)
    /** Database column member_id SqlType(INT), Default(None) */
    val memberId: Rep[Option[Int]] = column[Option[Int]]("member_id", O.Default(None))
    /** Database column tweet SqlType(VARCHAR), Length(140,true), Default(None) */
    val tweet: Rep[Option[String]] = column[Option[String]]("tweet", O.Length(140,varying=true), O.Default(None))
    /** Database column tweet_at SqlType(TIMESTAMP) */
    val tweetAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("tweet_at")

    /** Foreign key referencing Member (database name tweet_ibfk_1) */
    lazy val memberFk = foreignKey("tweet_ibfk_1", memberId, Member)(r => Rep.Some(r.memberId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Tweet */
  lazy val Tweet = new TableQuery(tag => new Tweet(tag))
}
