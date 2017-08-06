package api.models

import org.mindrot.jbcrypt.BCrypt
import play.api.data.validation.ValidationError
import play.api.libs.json.Json

case class Password(
  value: String
) {
  def hashed: HashedPassword = HashedPassword(BCrypt.hashpw(value, BCrypt.gensalt()))
}

object Password {
  val maxLength = 100

  implicit val reads = Json.reads[Password]
    .filter(ValidationError("error.password.overMaxLength"))(_.value.length < maxLength)
  implicit val writes = Json.writes[Password]
}
