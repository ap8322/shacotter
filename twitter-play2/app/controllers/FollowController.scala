package controllers

import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.Forms._
import models.auth.AuthConfigImpl
import models.dao.{FollowDAO, MemberDAO}
import play.api.cache.CacheApi
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FollowController @Inject()(val memberDAO: MemberDAO,
                                 val followDAO: FollowDAO,
                                 val cacheApi: CacheApi)
  extends Controller with AuthElement with AuthConfigImpl {

  import FollowController._

  /**
    * goto follower page
    *
    * @return
    */
  def followerlist = AsyncStack(AuthorityKey -> None) { implicit rs =>
    followDAO.selectFollowerList(loggedIn.memberId).map { memberList =>
      Ok(views.html.user.member(loggedIn.name, memberList, tweetForm))
    }
  }

  /**
    * add member to follow list
    *
    * @return
    */
  def follow = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[IdJson].map { form =>
      followDAO.follow(loggedIn.memberId, form.id).map { _ =>
        Ok(Json.obj("result" -> "success", "message" -> "add member to follow list"))
      }
    }.recoverTotal { e =>
      Future.successful(BadRequest(Json.obj("result" -> "failure", "message" -> JsError.toJson(e))))
    }
  }

  /**
    * remove member from follow list
    *
    * @return
    */
  def remove = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[IdJson].map { form =>
      followDAO.remove(loggedIn.memberId, form.id).map { _ =>
        Ok(Json.obj("result" -> "success", "message" -> "remove member from follow list"))
      }
    }.recoverTotal { e =>
      Future.successful(BadRequest(Json.obj("result" -> "failure", "message" -> JsError.toJson(e))))
    }
  }
}

object FollowController {

  // todo jsonのエラーハンドリング､学習

  case class IdJson(id: Int)

  implicit val Reader: Reads[IdJson] = (__ \ "id").read[Int].map(IdJson)
}