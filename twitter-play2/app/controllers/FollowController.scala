package controllers

import javax.inject.Inject

import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Forms._
import models.Tables._
import models.{AuthConfigImpl, MemberDAO, TweetDAO}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future

class FollowController @Inject()(val memberDAO: MemberDAO,
                                 val tweetDAO: TweetDAO)
  extends Controller with LoginLogout with AuthElement with AuthConfigImpl {

  import FollowController._

  def follow = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[idForm].map { form =>
      val follow = FollowRow(loggedIn.memberId, form.id)
      tweetDAO.follow(follow)
      Future.successful(Ok(Json.obj("result" -> form.id)))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  def unfollow = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[idForm].map { form =>
      val follow = FollowRow(loggedIn.memberId, form.id)
      tweetDAO.unfollow(follow)
      Future.successful(Ok(Json.obj("result" -> form.id)))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * goto follower page
    *
    * @return
    */
  def followerlist = AsyncStack(AuthorityKey -> None) { implicit rs =>
    tweetDAO.selectFollowerList(loggedIn.memberId).map { mem =>
      val m = mem.map(row =>
        MemberWithIsfollow(row.memberId, row.name, true)
      )
      Ok(views.html.user.member(loggedIn.name, m, tweetForm))
    }
  }
}

object FollowController {

  case class idForm(id: Int);
  //  implicit val userFormFormat = Json.reads[idForm]

  implicit val userFormFormat: Reads[idForm] = new Reads[idForm] {
    def reads(js: JsValue) = {
      JsSuccess(idForm(id = (js \ "id").as[Int]))
    }
  }
}
