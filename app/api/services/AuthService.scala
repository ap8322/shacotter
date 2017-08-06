package api.services

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import api.models.{Email, Member, Password}
import api.repositories.MemberRepository

class AuthService @Inject()(
    val repo: MemberRepository
)(
  implicit val ec: ExecutionContext
) {

  def invalidPasswordOrValid(email: Email, password: Password): Future[Either[String, Member]] = {
    repo.findBy(email).map {
      case Some(user) if user.password.checkpw(password) => Right(user)
      case _ => Left("メールまたはパスワードが違います｡")
    }
  }
}
