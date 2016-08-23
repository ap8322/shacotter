package models
import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
/**
  * Created by yuki.haneda on 2016/08/17.
  */

object Validater extends Validater


trait Validater {
  private val emaiRegex = """/^[a-zA-Z0-9_\.\-]+@[a-zA-Z0-9_\.\-]+$/""".r
  private val passwordRegex = """"""

  def email:Constraint[String] = Constraint[String]("constraint.custom.email") { e =>
    if ((e == null) || (e.trim.isEmpty)) Valid // use nonEmpty or custom nonEmpty constraints
    else emaiRegex.findFirstMatchIn(e)
      .map(_ => Valid)
      .getOrElse(Invalid(ValidationError("Emailの値が不正です｡")))
  }

//  def password: Constraint[String] = Constraint[String]("")
}
