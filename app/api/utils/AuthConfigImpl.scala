package api.utils

import controllers.routes
import jp.t2v.lab.play2.auth.{
  AsyncIdContainer,
  AuthConfig,
  CookieTokenAccessor
}

import models.Tables.MemberRow
import models.dao.MemberDAO
import play.api.cache.CacheApi
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}

import api.models.Member
import api.repositories.MemberRepository

import models.auth.MemcachedIdContainer

trait AuthConfigImpl extends AuthConfig {

  // Property
  type Id = Long
  type User = Member
  type Authority = None.type
  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds: Int = 3600

  val memberDAO: MemberRepository
  val cacheApi: CacheApi

  def resolveUser(id: Id)(
      implicit ctx: ExecutionContext): Future[Option[User]] =
    memberDAO.findBy(id).map(_.map(m => Member(m.memberId, m.name)))

  def loginSucceeded(request: RequestHeader)(
      implicit ctx: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.TweetController.timeline()))
  }

  def logoutSucceeded(request: RequestHeader)(
      implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.AuthController.login))

  def authenticationFailed(request: RequestHeader)(
      implicit ctx: ExecutionContext): Future[Result] = {
    Future
      .successful(Redirect(routes.AuthController.login))
      .map(
        _.flashing(
          "success" -> "ログインし直してください｡"
        ))
  }

  override def authorizationFailed(
      request: RequestHeader,
      user: User,
      authority: Option[Authority])(implicit context: ExecutionContext) = {
    Future.successful(Forbidden("no permission"))
  }

  def authorize(user: User, authority: Authority)(
      implicit ctx: ExecutionContext): Future[Boolean] =
    Future.successful(true)

  // セッションを保存先をmemcachedに変更
  override lazy val idContainer: AsyncIdContainer[Id] = AsyncIdContainer(
    new MemcachedIdContainer[Id](cacheApi))

  // sesstion
  override lazy val tokenAccessor = new CookieTokenAccessor(
    cookieSecureOption = false,
    cookieMaxAge = Some(sessionTimeoutInSeconds)
  )
}
