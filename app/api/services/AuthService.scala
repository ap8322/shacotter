package api.services

import javax.inject.Inject

import scala.concurrent.Future

import api.repositories.MemberRepository
import org.mindrot.jbcrypt.BCrypt

class AuthService @Inject()(
    val repo: MemberRepository
) {

  def invalidPasswordOrValid(
      email: String,
      password: String): Future[Either[String, Long]] = {
    repo.findBy(email).map {
      case Some(user) if checkpw(password, user.password) =>
        Right(user.memberId)
      case _ => Left("メールまたはパスワードが違います｡")
    }
  }

  private[this] def checkpw(password: String,
                            storedPassword: String): Boolean = {
    BCrypt.checkpw(password, storedPassword)
  }
}
