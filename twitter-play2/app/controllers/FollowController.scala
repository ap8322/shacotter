package controllers

import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.Tables._
import models.{AuthConfigImpl, MemberDAOLike, Tables, TweetDAO}
import play.api.data.Forms._
import play.api.data._
import play.api.db.slick._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

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
                                 val memberDAO: MemberDAOLike,
                                 val tweetDAO: TweetDAO) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport with AuthElement with AuthConfigImpl {

  import FollowController._

  def list = AsyncStack(AuthorityKey -> None) { implicit rs =>
    db.run(Tables.Member.sortBy(m => m.memberId).result).map { mem =>
      Ok(views.html.user.member(loggedIn.name, mem, tweetForm))
    }
  }

  def follow(followId: Int) = AsyncStack(AuthorityKey -> None) { implicit rs =>
    val follow = FollowRow(loggedIn.id, followId)
    tweetDAO.follow(follow)
    Future.successful(Redirect(routes.FollowController.followerlist))
  }

  def followerlist = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetDAO.selectFollowerList(loggedIn.id).map { mem =>
      Ok(views.html.user.member(loggedIn.name, mem, tweetForm))
    }
  }
}
