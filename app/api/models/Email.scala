package api.models

import play.api.data.validation.ValidationError
import play.api.libs.json.Json

case class Email(
   value: String
){
  def isValid = Email.regex.findFirstIn(this.value).isDefined
}

object Email {
  // https://www.w3.org/TR/html5/forms.html#valid-e-mail-address
  val regex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
  val maxLength = 100

  implicit val reads = Json.reads[Email]
    .filter(ValidationError("error.email.overMaxLength"))(_.value.length < maxLength)
    .filter(ValidationError("error.email.invalidMailPattern"))(_.isValid)

  implicit val writes = Json.writes[Email]
}
