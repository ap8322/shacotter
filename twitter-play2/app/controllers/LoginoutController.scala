package controllers

/**
  * Created by yuki.haneda on 2016/08/02.
  */

import javax.inject.Inject

import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.{AuthConfigImpl, LoginForm, MemberDAOLike}
import models.Tables.MemberRow
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

object LoginoutController {

  val loginForm = Form(
    mapping(
      "email" -> nonEmptyText(maxLength = 20),
      "password" -> nonEmptyText(maxLength = 20)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  case class SignupForm(name: String,email: String,password: String)

  val signupForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 20),
      "email" -> nonEmptyText(maxLength = 20),
      "password" -> nonEmptyText(maxLength = 20)
    )(SignupForm.apply)(SignupForm.unapply)
  )
}

class LoginoutController @Inject()(val memberDAO: MemberDAOLike)
  extends Controller with LoginLogout with AuthElement with AuthConfigImpl {

  import LoginoutController._

  def index() = Action { implicit request =>
    Ok(views.html.auth.signin(loginForm))
  }

  def login() = Action.async { implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.auth.signin(formWithErrors)))
        },
        form => {
          memberDAO.authenticate(form).flatMap {
            case Some(user) =>
              gotoLoginSucceeded(user.id)
            case _ =>
              Future.successful(Unauthorized(views.html.auth.signin(loginForm.fill(form).withGlobalError("メールまたはパスワードが違います｡"))))
          }
        }
      )
    }

  def logout() = Action.async { implicit request =>
      gotoLogoutSucceeded
  }

  def signup() = Action { implicit request =>
    Ok(views.html.auth.signup(signupForm))
  }

  def create() = Action.async { implicit request =>
    signupForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.signup(formWithErrors)))
      },
      form => {
        val member = MemberRow(0,form.email,form.password,"solt",form.name)
        memberDAO.create(member).flatMap{
          case Some(user) => gotoLoginSucceeded(user.id)
          case _ => Future.successful(Unauthorized(views.html.auth.signup(signupForm.fill(form).withGlobalError("メールまたはパスワードが違います｡"))))
        }
      }
    )
  }
}