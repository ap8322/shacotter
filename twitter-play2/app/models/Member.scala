package models

import models.Tables._

/**
  * Created by yuki.haneda on 2016/08/03.
  */
case class Member(id: Int , email: String, password: String, passwordsalt: String, name: String){
    def this(row: MemberRow) = this(
      row.id,
      row.email,
      row.password,
      row.passwordsalt,
      row.name
    )
}
