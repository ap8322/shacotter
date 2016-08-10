package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import jp.t2v.lab.play2.auth.AuthElement
import models.Forms._
import models.Tables._
import models.{AuthConfigImpl, MemberDAO, TweetDAO}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class FollowController @Inject()(val memberDAO: MemberDAO,
                                 val tweetDAO: TweetDAO)
  extends Controller with AuthElement with AuthConfigImpl {

  def list = AsyncStack(AuthorityKey -> None) { implicit rs =>
    memberDAO.selectList().map { mem =>
      Ok(views.html.user.member(loggedIn.name, mem, tweetForm))
    }
  }

  def follow(followId: Int) = AsyncStack(AuthorityKey -> None) { implicit rs =>
    val follow = FollowRow(loggedIn.memberId, followId)
    tweetDAO.follow(follow)
    Future.successful(Redirect(routes.FollowController.followerlist))
  }

  def followerlist = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetDAO.selectFollowerList(loggedIn.memberId).map { mem =>
      Ok(views.html.user.member(loggedIn.name, mem, tweetForm))
    }
  }
}
