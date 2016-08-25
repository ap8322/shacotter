package controllers

/**
  * Created by yuki.haneda on 2016/08/18.
  */


import javax.inject.Inject

import controllers.EvalController.TweetIdForm
import jp.t2v.lab.play2.auth.AuthElement
import models.dao.{EvalDAO, MemberDAO}
import models.Tables._
import models.auth.AuthConfigImpl
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
  def addEval() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[TweetIdForm].map { form =>
      evalDAO.insert(EvalRow(form.tweetId, form.evalStatus, loggedIn.memberId)).map(_ =>
        Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus))
      )
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * [good -> bad] or [bad -> good]
    *
    * @return
    */
  def updateEval() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[TweetIdForm].map { form =>
      evalDAO.update(EvalRow(form.tweetId, form.evalStatus, loggedIn.memberId)).map(_ =>
        Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus))
      )
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * good or bad -> None
    *
    * @return
    */
  def deleteEval() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[TweetIdForm].map { form =>
      evalDAO.delete(form.tweetId, loggedIn.memberId).map(_ =>
        Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus))
      )
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
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
      )
      )
    }
  }
}