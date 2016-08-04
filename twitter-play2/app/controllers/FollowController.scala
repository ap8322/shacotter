package controllers

import java.util.Date

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.db.slick._
import slick.driver.JdbcProfile
import models.Tables._
import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.{AuthConfigImpl, MemberDAOLike, Tables}

import scala.concurrent.Future
import slick.driver.MySQLDriver.api._

object FollowController {

  case class Tweet(tweet: String)

  val tweetForm = Form(
    mapping(
      "tweet" -> nonEmptyText(maxLength = 140)
    )(Tweet.apply)(Tweet.unapply)
  )
}

class FollowController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                                 val messagesApi: MessagesApi,
                                 val memberDAO: MemberDAOLike) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport with AuthElement with AuthConfigImpl {

  import FollowController._

  def list = AsyncStack(AuthorityKey -> None) { implicit rs =>
    db.run(Tables.Member.sortBy(m => m.id).result).map { mem =>
      Ok(views.html.user.member(loggedIn.name,mem,tweetForm))
    }
  }

  def follow = AsyncStack(AuthorityKey -> None) { implicit rs =>
    db.run(Tables.Member.sortBy(m => m.id).result).map { mem =>
      Ok(views.html.user.member(loggedIn.name,mem,tweetForm))
    }
  }

  def followerlist = TODO
}
