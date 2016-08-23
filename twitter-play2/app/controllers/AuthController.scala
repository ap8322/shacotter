package controllers

/**
  * Created by yuki.haneda on 2016/08/02.
  */
import com.google.inject.Inject
import jp.t2v.lab.play2.auth.LoginLogout
import models.Forms._
import models.DAO.MemberDAO
import models.auth.AuthConfigImpl
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class AuthController @Inject()(val memberDAO: MemberDAO,
                               val cacheApi: CacheApi)
  extends Controller with LoginLogout with AuthConfigImpl {

  /**
    * goto login page
    *
    * @return
    */
  def login() = Action { implicit rs =>
    Ok(views.html.auth.login(loginForm))
  }

  /**
    * remove session and redirect login page
    *
    * @return
    */
  def logout() = Action.async { implicit rs =>
    gotoLogoutSucceeded.map(_.flashing(
      "success" -> "You've been logged out"
    ))
  }

  /**
    * authentication and create session
    *
    * @return
    */
  def auth() = Action.async { implicit rs =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.auth.login(formWithErrors)))
      },
      form => {
        memberDAO.authenticate(form).flatMap {
          case Some(user) =>
            gotoLoginSucceeded(user.memberId)
          case None =>
            Future.successful(Unauthorized(views.html.auth.login(loginForm.fill(form).withGlobalError("メールまたはパスワードが違います｡"))))
        }
      }
    )
  }

  /**
    * goto signup page
    *
    * @return
    */
  def signup() = Action { implicit rs =>
    Ok(views.html.auth.signup(statusForm))
  }
}