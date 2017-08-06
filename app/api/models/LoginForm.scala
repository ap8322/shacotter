package api.models

import play.api.libs.json.Json

case class LoginForm (
  email: Email,
  password: Password
)

object LoginForm {
  // TODO { email: { value: "hogehoge" }} 的な感じになっているので、明示的に指定するかマクロの状態でできるかを調査してみる。
  implicit val format = Json.format[LoginForm]
}
