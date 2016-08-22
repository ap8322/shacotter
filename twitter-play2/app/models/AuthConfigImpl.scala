package models

import controllers.routes
import jp.t2v.lab.play2.auth.{AsyncIdContainer, AuthConfig, CookieIdContainer, CookieTokenAccessor}
import models.Tables.MemberRow
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}

trait AuthConfigImpl extends AuthConfig {

  type Id = Int
  type User = MemberRow
  type Authority = None.type

  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds: Int = 3600
  val memberDAO: MemberDAO


  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = memberDAO.findById(id)

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.TweetController.timeline))
  }

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.AuthController.login))

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.AuthController.login)).map(_.flashing(
      "success" -> "ログインし直してください｡"
    ))
  }

  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit context: ExecutionContext) = {
    Future.successful(Forbidden("no permission"))
  }

  /**
    * A function that determines what `Authority` a user has.
    * You should alter this procedure to suit your application.
    */
  //  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful {
  //    (user.role, authority) match {
  //      case (Administrator, _)       => true
  //      case (NormalUser, NormalUser) => true
  //      case _                        => false
  //    }
  //  }
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful(true)

  /**
    * (Optional)
    * You can custom SessionID Token handler.
    * Default implementation use Cookie.
    */
  override lazy val tokenAccessor = new CookieTokenAccessor(
    /*
     * Whether use the secure option or not use it in the cookie.
     * Following code is default.
     */
    cookieSecureOption = false,
    cookieMaxAge = Some(sessionTimeoutInSeconds)
  )
}
