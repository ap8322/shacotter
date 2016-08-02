package controllers

/**
  * Created by yuki.haneda on 2016/08/02.
  */
  import javax.inject.Inject

  import controllers.auth.AuthConfigImpl
  import jp.t2v.lab.play2.auth.LoginLogout
  import models.Account
  import play.api.data.Form
  import play.api.data.Forms._
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  import play.api.mvc.{Action, Controller}

  import scala.concurrent.Future

  /**
    * Created by yuki.haneda on 2016/08/01.
    */
  class LoginoutController @Inject() extends Controller with LoginLogout with AuthConfigImpl {

    val loginForm = Form {
      mapping("email" -> email, "password" -> text)(Account.authenticate)(_.map(u => (u.email, "")))
        .verifying("emailまたはpasswordが正しくありません｡", result => result.isDefined)
    }

    /** Alter the login page action to suit your application. */
    def login = Action { implicit request =>
      Ok(views.html.user.login(loginForm))
    }

    /**
      * Return the `gotoLogoutSucceeded` method's result in the logout action.
      *
      * Since the `gotoLogoutSucceeded` returns `Future[Result]`,
      * you can add a procedure like the following.
      *
      *   gotoLogoutSucceeded.map(_.flashing(
      *     "success" -> "You've been logged out"
      *   ))
      */
    def logout = Action.async { implicit request =>
      // do something...
      gotoLogoutSucceeded
    }

    /**
      * Return the `gotoLoginSucceeded` method's result in the login action.
      *
      * Since the `gotoLoginSucceeded` returns `Future[Result]`,
      * you can add a procedure like the `gotoLogoutSucceeded`.
      */
    def authenticate = Action.async { implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => Future.successful(BadRequest(views.html.user.login(formWithErrors))),
        user => gotoLoginSucceeded(user.get.id)
      )
    }
  }

