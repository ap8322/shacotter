package controllers

import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.{AuthConfigImpl, MemberDAOLike, Tables}
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

object TweetController {
  case class Tweet(tweet: String)

  val tweetForm = Form(
    mapping(
      "tweet" -> nonEmptyText(maxLength = 140)
    )(Tweet.apply)(Tweet.unapply)
  )
}

import TweetController._

class TweetController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                                val messagesApi: MessagesApi,
                                val memberDAO: MemberDAOLike) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport with AuthElement with AuthConfigImpl {

  def index() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    db.run(Tables.Tweet.filter(_.name === loggedIn.name.bind).result).map{ tweet =>
      Ok(views.html.user.list(tweet,tweetForm))
    }
  }

  def tweet() = AsyncStack(AuthorityKey -> None){ implicit rs =>
    tweetForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(Redirect(routes.TweetController.index))
        },
        form => {
          val tweet = Tables.TweetRow(loggedIn.name,Some(form.tweet))
          db.run(Tables.Tweet += tweet).map { _ =>
            Redirect(routes.TweetController.index)
          }
        }
      )
  }

  def timeline() = TODO
}