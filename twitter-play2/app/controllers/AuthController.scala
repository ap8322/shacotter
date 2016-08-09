package controllers

/**
  * Created by yuki.haneda on 2016/08/02.
  */

import javax.inject.Inject

import controllers.MemberController.statusForm
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.{AuthConfigImpl, LoginForm, MemberDAO}
import play.api.Environment
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

object AuthController {
  val loginForm = Form(
    mapping(
      "email" -> nonEmptyText(maxLength = 20).verifying(),
      "password" -> nonEmptyText(maxLength = 20)
    )(LoginForm.apply)(LoginForm.unapply)
  )
}

class AuthController @Inject()(val memberDAO: MemberDAO,
                               val environment: Environment)
  extends Controller with LoginLogout with AuthElement with AuthConfigImpl {

  import AuthController._

  def index() = Action { implicit request =>
    Ok(views.html.auth.signin(loginForm))
  }

  def signup() = Action { implicit request =>
    Ok(views.html.auth.signup(statusForm))
  }

  def login() = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.signin(formWithErrors)))
      },
      form => {
        memberDAO.authenticate(form).flatMap {
          case Some(user) =>
            gotoLoginSucceeded(user.memberId)
          case _ =>
            Future.successful(Unauthorized(views.html.auth.signin(loginForm.fill(form).withGlobalError("メールまたはパスワードが違います｡"))))
        }
      }
    )
  }

  def logout() = Action.async { implicit request =>
    gotoLogoutSucceeded.map(_.flashing(
      "success" -> "You've been logged out"
    ))
  }
}