package controllers.auth

import com.sun.xml.internal.bind.v2.TODO
import controllers.routes
import jp.t2v.lab.play2.auth.{AuthConfig, CookieTokenAccessor}
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}
/**
  * Created by yuki.haneda on 2016/08/01.
  */

trait AuthConfigImpl extends AuthConfig {

  type Id = Int
  type User = Member
  type Authority = None.type
  val  idTag: ClassTag[Id] = classTag[Id]
  val  sessionTimeoutInSeconds: Int = 3600
  val memberDAO : MemberDAOLike

  /**
    * A function that returns a `User` object from an `Id`.
    * You can alter the procedure to suit your application.
    */
  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = memberDAO.findById(id)

  /**
    * Where to redirect the user after a successful login.
    */
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) =
  Future.successful(Redirect(routes.UserController.list))

  /**
    * Where to redirect the user after logging out
    */
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) =
  Future.successful(Redirect(routes.LoginoutController.index))

  /**
    * If the user is not logged in and tries to access a protected resource then redirect them as follows:
    */
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) =
  Future.successful(Redirect(routes.LoginoutController.index))

  /**
    * If authorization failed (usually incorrect password) redirect the user as follows:
    */
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
    cookieSecureOption = play.api.Play.isProd(play.api.Play.current),
    cookieMaxAge       = Some(sessionTimeoutInSeconds)
  )
}
