package controllers

import javax.inject.Inject

import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Tables._
import models.{AuthConfigImpl, MemberDAO}
import play.api.Environment
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Controller

import scala.concurrent.Future

object MemberController {

  case class StatusForm(name: String, email: String, password: String)

  val statusForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 20),
      "email" -> nonEmptyText(maxLength = 20),
      "password" -> nonEmptyText(maxLength = 20)
    )(StatusForm.apply)(StatusForm.unapply)
  )
}

import controllers.MemberController._

class MemberController @Inject()(val environment: Environment,
                                 val memberDAO: MemberDAO)
  extends Controller with LoginLogout with AuthElement with AuthConfigImpl {

  def create() = AsyncStack(AuthorityKey -> None) { implicit request =>
    statusForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.signup(formWithErrors)))
      },
      form => {
        val member = MemberRow(0, form.email, form.password, form.name)
        memberDAO.create(member).flatMap {
          case Some(user) => gotoLoginSucceeded(user.memberId)
          case _ => Future.successful(Unauthorized(views.html.auth.signup(statusForm.fill(form).withGlobalError("メールまたはパスワードが違います｡"))))
        }
      }
    )
  }

  def edit() = AsyncStack(AuthorityKey -> None) { implicit request =>
    memberDAO.findById(1).map {
      case Some(member) => Ok(views.html.user.edit(statusForm.fill(StatusForm(member.name, member.email, member.password))))
      case _ => Ok(views.html.user.edit(statusForm.fill(StatusForm("", "", ""))))
    }
  }

  def update() = AsyncStack(AuthorityKey -> None) { implicit request =>
    statusForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.signup(formWithErrors)))
      },
      form => {
        val member = MemberRow(0, form.email, form.password, form.name)
        memberDAO.update(member)
        Future.successful(Redirect(routes.TweetController.index))
      }
    )
  }

  def delete() = ???
}
