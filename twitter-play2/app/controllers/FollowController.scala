package controllers

import com.google.inject.Inject
import jp.t2v.lab.play2.auth.AuthElement
import models.DAO.{FollowDAO, MemberDAO}
import models.Forms._
import models.Tables._
import models.auth.AuthConfigImpl
import play.api.cache.CacheApi
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

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
    rs.body.validate[idForm].map { form =>
      val follow = FollowRow(loggedIn.memberId, form.id)

      followDAO.follow(follow).onComplete {
        case Success(s) => ???
        case Failure(t) => ???
      }


      Future.successful(Ok(Json.obj(
        "result" -> form.id
      )))
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj(
          "result" -> "failure",
          "error" -> JsError.toJson(e)
        ))
      }
    }
  }

  /**
    * remove member from follow list
    *
    * @return
    */
  def remove = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[idForm].map { form =>
      val follow = FollowRow(loggedIn.memberId, form.id)
      followDAO.remove(follow)

      Future.successful(Ok(Json.obj(
        "result" -> form.id
      )))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj(
          "result" -> "failure",
          "error" -> JsError.toJson(e)
        ))
      }
    }
  }
}

object FollowController {

  case class idForm(id: Int)

  implicit val userFormFormat: Reads[idForm] = new Reads[idForm] {
    def reads(js: JsValue): JsResult[idForm] = {
      JsSuccess(idForm(id = (js \ "id").as[Int]))
    }
  }
}