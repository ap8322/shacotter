package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import jp.t2v.lab.play2.auth.AuthElement
import models.Forms._
import models.{AuthConfigImpl, MemberDAO, Tables, TweetDAO}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Controller

import scala.concurrent.Future

@Singleton
class TweetController @Inject()(val memberDAO: MemberDAO,
                                val tweetDAO: TweetDAO)
  extends Controller with AuthElement with AuthConfigImpl {

  def index() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetDAO.selectMyTweet(loggedIn.memberId).map { tweet =>
      Ok(views.html.user.list(loggedIn.name, tweet, tweetForm))
    }
  }

  def tweet() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.TweetController.index))
      },
      form => {
        val tweet = Tables.TweetRow(0, Some(loggedIn.memberId), Some(form.tweet))
        tweetDAO.add(tweet)
        Future.successful(Redirect(routes.TweetController.index))
      }
    )
  }

  def timeline() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetDAO.selectFollowerTweet(loggedIn.memberId).map { tweet =>
      Ok(views.html.user.list(loggedIn.name, tweet, tweetForm))
    }
  }

  def ajaxCall = StackAction(AuthorityKey -> None){implicit request =>
    Ok("Ajax Call!")
  }
}