package api.controllers

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import api.services.AuthService
import api.utils.AuthConfigImpl
import jp.t2v.lab.play2.auth.LoginLogout
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}

import models.Forms.loginForm

class AuthController @Inject()(
    val cacheApi: CacheApi,
    val service: AuthService
)(
    implicit val ec: ExecutionContext
) extends Controller
    with AuthConfigImpl
    with LoginLogout {

  val memberDAO = service.repo

  /**
    * authentication and create session
    */
  def auth() = Action.async { implicit rs =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.login(formWithErrors)))
      },
      form => {
        service.invalidPasswordOrValid(form.email, form.password).flatMap {
          case Left(error) =>
            Future.successful(BadRequest(views.html.auth
              .login(loginForm.fill(form).withGlobalError(error))))
          case Right(userId) =>
            gotoLoginSucceeded(userId)
        }
      }
    )
  }
}
