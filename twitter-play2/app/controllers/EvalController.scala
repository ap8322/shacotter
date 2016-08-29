package controllers

/**
  * Created by yuki.haneda on 2016/08/18.
  */

import javax.inject.Inject

import controllers.EvalController.TweetIdForm
import jp.t2v.lab.play2.auth.AuthElement
import models.auth.AuthConfigImpl
import models.dao.{EvalDAO, MemberDAO}
import play.api.cache.CacheApi
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EvalController @Inject()(val memberDAO: MemberDAO,
                               val evalDAO: EvalDAO,
                               val cacheApi: CacheApi)
  extends Controller with AuthElement with AuthConfigImpl {

  /**
    * None -> good of bad
    *
    * @return
    */
  def addStatus() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[TweetIdForm].map { form =>
      evalDAO.insert(form.tweetId, form.evalStatus, loggedIn.memberId).map(_ =>
        Ok(Json.obj("result" -> form.tweetId, "message" -> form.evalStatus))
      )
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "message" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * [good -> bad] or [bad -> good]
    *
    * @return
    */
  def updateStatus() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[TweetIdForm].map { form =>
      evalDAO.update(form.tweetId, form.evalStatus, loggedIn.memberId).map(_ =>
        Ok(Json.obj("result" -> form.tweetId, "message" -> form.evalStatus))
      )
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "message" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * good or bad -> None
    *
    * @return
    */
  def deleteStatus() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[TweetIdForm].map { form =>
      evalDAO.delete(form.tweetId, loggedIn.memberId).map(_ =>
        Ok(Json.obj("result" -> form.tweetId, "message" -> form.evalStatus))
      )
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "message" -> JsError.toJson(e)))
      }
    }
  }
}

object EvalController {

  case class TweetIdForm(tweetId: Int, evalStatus: Int)

  implicit val tweetFormFormat: Reads[TweetIdForm] = new Reads[TweetIdForm] {
    override def reads(json: JsValue): JsResult[TweetIdForm] = {
      JsSuccess(TweetIdForm(
        tweetId = (json \ "tweet_id").as[Int],
        evalStatus = (json \ "eval_status").as[Int]
      ))
    }
  }
}