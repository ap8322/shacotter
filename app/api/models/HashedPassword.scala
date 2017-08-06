package api.models

import org.mindrot.jbcrypt.BCrypt

case class HashedPassword(
                    value: String
                    ) {
  def checkpw(password: Password): Boolean = BCrypt.checkpw(password.value, this.value)
}
