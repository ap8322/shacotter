package models
import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
/**
  * Created by yuki.haneda on 2016/08/17.
  */
trait Validater {
  private val emaiRegex = """/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/""".r

  def email:Constraint[String] = Constraint[String]("constraint.custom.email") { e =>
    if ((e == null) || (e.trim.isEmpty)) Valid // use nonEmpty or custom nonEmpty constraints
    else emaiRegex.findFirstMatchIn(e)
      .map(_ => Valid)
      .getOrElse(Invalid(ValidationError("validation.error.invalid.email")))
  }
}
