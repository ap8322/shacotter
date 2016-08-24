package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.DAO.{FollowDAO, MemberDAO}
import models.Forms._
import models.Tables._
import models.auth.AuthConfigImpl
import org.mindrot.jbcrypt.BCrypt
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
        val hashpw = BCrypt.hashpw(form.password, BCrypt.gensalt())
        val member = MemberRow(1, form.email, hashpw, form.name)

        memberDAO.findByEmail(form.email).flatMap {
          case Some(user) => Future.successful(BadRequest(views.html.auth.signup(statusForm.fill(form).withError("mail", "申し訳ございませんが､既に使用されているメールアドレスです｡"))))
          case _ => memberDAO.create(member).flatMap {
            case Some(user) => {
              gotoLoginSucceeded(user.memberId)
            }
            case _ => Future.successful(Unauthorized(views.html.auth.signup(statusForm.fill(form).withGlobalError("メールまたはパスワードが違います｡"))))
          }
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
      case _ => Ok(views.html.user.edit(statusForm.fill(StatusForm("", "", ""))))
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
        val member = MemberRow(0, form.email, form.password, form.name)
        memberDAO.findByEmail(form.email).flatMap {
          case Some(user) => Future.successful(Ok(views.html.user.edit(statusForm.fill(StatusForm("", "", "")).withGlobalError("申し訳ございませんが､既に使用されているメールアドレスです｡"))))
          case _ => {
            memberDAO.update(member)
            Future.successful(Redirect(routes.TweetController.index))
          }
        }
      }
    )
  }

  def delete() = ???
}
