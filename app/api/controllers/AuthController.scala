package api.controllers

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import api.models.LoginForm
import api.services.AuthService
import api.utils.AuthConfigImpl
import jp.t2v.lab.play2.auth.LoginLogout
import play.api.cache.CacheApi
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}

class AuthController @Inject()(
    val cacheApi: CacheApi,
    val service: AuthService
)(
    implicit val ctx: ExecutionContext
) extends Controller
    with AuthConfigImpl
    with LoginLogout {

  // AuthConfigImplの関係で宣言しているが、できるだけcontrollerにrepositoryの依存を扠せたくない。
  val memberDAO = service.repo

  /**
    * TODO eitherT 化してfor式で記述できるようにする。
    * authentication and create session
    */
  def login() = Action.async(parse.json) { implicit req =>
    req.body.validate[LoginForm].fold(
      errors => {
        Future.successful(
          BadRequest(
            Json.obj(
              "result" -> "failure",
              "message" -> JsError.toJson(errors))
          )
        )
      },
      form => {
        service.invalidPasswordOrValid(
          form.email,
          form.password
        ).flatMap {
          case Left(error) =>
            Future.successful(
              BadRequest(
                Json.obj(
                  "result" -> "failure",
                  "message" -> error)
              )
            )
          case Right(user) =>
            gotoLoginSucceeded(user.memberId)
        }
      }
    )
  }
}
