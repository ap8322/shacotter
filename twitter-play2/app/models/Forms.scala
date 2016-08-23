package models

import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by yuki.haneda on 2016/08/10.
  */
object Forms {

  // todo バリデーションを固める｡

  case class LoginForm(email: String, password: String)

  val loginForm = Form(
    mapping(
      "email" -> nonEmptyText(maxLength = 200),
      "password" -> nonEmptyText(maxLength = 200)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  case class Tweet(tweet: String)

  val tweetForm = Form(
    mapping(
      "tweet" -> nonEmptyText(maxLength = 140)
    )(Tweet.apply)(Tweet.unapply)
  )

  case class StatusForm(name: String, email: String, password: String)

  val statusForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 20),
      "email" -> nonEmptyText(maxLength = 200),
      "password" -> nonEmptyText(maxLength = 10000)
    )(StatusForm.apply)(StatusForm.unapply)
  )

  // ツイートした人の名前、ツイートのユニークID、ツイートの内容、いいねの数、悪いねの数
  case class TweetInfo(name: String, tweet_id: Int, tweet: String, good: Int, bad: Int)
}
