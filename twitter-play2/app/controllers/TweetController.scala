package controllers

import javax.inject.Inject

import controllers.auth.{AuthConfigImpl, MemberDAOLike}
import jp.t2v.lab.play2.auth.AuthElement
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import slick.driver.JdbcProfile

/**
  * Created by yuki.haneda on 2016/08/03.
  */
object TweetController {
  case class Tweet(name : String,tweet: String)

  val tweet = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 20),
      "tweet" -> nonEmptyText(maxLength = 140)
    )(Tweet.apply)(Tweet.unapply)
  )
}

class TweetController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                                val messagesApi: MessagesApi,
                                val memberDAO: MemberDAOLike) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport with AuthElement with AuthConfigImpl {

  def index() = TODO

  def tweet() = TODO
}
