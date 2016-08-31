package controllers


import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.Forms._
import models.auth.AuthConfigImpl
import models.dao.{MemberDAO, TweetDAO}
import play.api.cache.CacheApi
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TweetController @Inject()(val memberDAO: MemberDAO,
                                val tweetDAO: TweetDAO,
                                val cacheApi: CacheApi)
  extends Controller with AuthElement with AuthConfigImpl {

  /**
    * goto my tweet page
    *
    * @return
    */
  def index() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    val id = loggedIn.memberId
    for {
      tweetInfo <- tweetDAO.selectMyTweet(id)
      memberInfo <- memberDAO.selectMemberInfo(id)
    } yield Ok(views.html.user.list(memberInfo, tweetInfo, tweetForm))
  }

  /**
    * goto other member profile tweet page
    *
    * @param id other_member_id
    * @return
    */
  def profile(id: Long) = AsyncStack(AuthorityKey -> None) { implicit rs =>
    val loginId = loggedIn.memberId
    for {
      tweetInfo <- tweetDAO.selectFriendTweet(loginId, id)
      memberInfo <- memberDAO.selectMemberInfo(id)
    } yield Ok(views.html.user.list(memberInfo, tweetInfo, tweetForm))
  }

  /**
    * goto timeline page
    *
    * @return
    */
  def timeline() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    val id = loggedIn.memberId
    for {
      tweetInfo <- tweetDAO.selectFollowerTweet(id)
      memberInfo <- memberDAO.selectMemberInfo(id)
    } yield Ok(views.html.user.list(memberInfo, tweetInfo, tweetForm))
  }

  /**
    * tweet and redirect timeline page
    *
    * @return
    */
  def tweet() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.TweetController.timeline))
      },
      form => {
        tweetDAO.add(loggedIn.memberId, form.tweet).map(_ => Redirect(routes.TweetController.timeline))
      }
    )
  }
}