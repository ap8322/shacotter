package controllers.auth

/**
  * Created by yuki.haneda on 2016/08/01.
  */

import jp.t2v.lab.play2.auth.AuthElement
import models.Role.NormalUser
import play.api.mvc.Controller
import views.html

trait Messages extends Controller with AuthElement with AuthConfigImpl {

  def main = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(views.html.index("dummy"))
  }
}
object Messages extends Messages
