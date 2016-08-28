package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Forms._
import models.auth.AuthConfigImpl
import models.dao.{FollowDAO, MemberDAO}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class MemberController @Inject()(val memberDAO: MemberDAO,
                                 val followDAO: FollowDAO,
                                 val cacheApi: CacheApi)
  extends Controller with LoginLogout with AuthElement with AuthConfigImpl {

  /**
    * select all member
    *
    * @return
    */
  def list = AsyncStack(AuthorityKey -> None) { implicit rs =>
    followDAO.selectFollowerList(loggedIn.memberId).map { mem =>
      Ok(views.html.user.member(loggedIn.name, mem, tweetForm))
    }
  }

  /**
    * create new member by using form data
    *
    * @return
    */
  def create() = Action.async { implicit rs =>
    statusForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.signup(formWithErrors)))
      },
      form => {
        memberDAO.findByEmail(form.email).flatMap {
          case Some(user) => Future.successful(
            BadRequest(views.html.auth.signup(statusForm.fill(form).withError("mail", "既に使用されているメールアドレスです｡")))
          )
          case _ =>
            for {
              _ <- memberDAO.create(form.email, form.password, form.name)
              newMember <- memberDAO.findByEmail(form.email)
              result <- gotoLoginSucceeded(newMember.get.memberId)
            } yield result
        }
      }
    )
  }

  /**
    * goto edit page with current status
    *
    * @return
    */
  def edit() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    memberDAO.findById(loggedIn.memberId).map {
      case Some(member) => Ok(views.html.user.edit(statusForm.fill(StatusForm(member.name, member.email, member.password))))
      case _ => BadRequest
    }
  }

  /**
    * update member's status
    *
    * @return
    */
  def update() = AsyncStack(AuthorityKey -> None) { implicit rs =>
    statusForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.user.edit(formWithErrors.withGlobalError("不正な値です｡"))))
      },
      form => {
        memberDAO.findByEmail(form.email).flatMap {
          case Some(user) if user.memberId != loggedIn.memberId => Future.successful(
            Ok(views.html.user.edit(statusForm.fill(form).withError("mail", "既に使用されているメールアドレスです｡")))
          )
          case _ => memberDAO.update(loggedIn.memberId, form.email, form.password, form.name).map(_ => Redirect(routes.TweetController.index))
        }
      }
    )
  }

  def delete() = TODO
}