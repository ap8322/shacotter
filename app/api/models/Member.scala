package api.models

case class Member(
    memberId: Long,
    name: String,
    password: HashedPassword
)
