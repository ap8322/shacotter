package controllers

/**
  * Created by yuki.haneda on 2016/08/18.
  */

import com.google.inject.Inject
import controllers.EvalController.tweetIdForm
import jp.t2v.lab.play2.auth.AuthElement
import models.DAO.{EvalDAO, MemberDAO}
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
    rs.body.validate[tweetIdForm].map { form =>
      evalDAO.insertStatus(EvalRow(form.tweetId, form.evalStatus, loggedIn.memberId))

      Future.successful(Ok(Json.obj(
        "result" -> form.tweetId,
        "eval_status" -> form.evalStatus
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

  /**
    * [good -> bad] or [bad -> good]
    *
    * @return
    */
  def updateEval() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[tweetIdForm].map { form =>
      evalDAO.updateStatus(EvalRow(form.tweetId, form.evalStatus, loggedIn.memberId))

      Future.successful(Ok(Json.obj(
        "result" -> form.tweetId,
        "eval_status" -> form.evalStatus
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

  /**
    * good or bad -> None
    *
    * @return
    */
  def deleteEval() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[tweetIdForm].map { form =>
      evalDAO.deleteStatus(form.tweetId, loggedIn.memberId)

      Future.successful(Ok(Json.obj(
        "result" -> form.tweetId,
        "eval_status" -> form.evalStatus
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

object EvalController {

  case class tweetIdForm(tweetId: Int, evalStatus: Int)

  implicit val tweetFormFormat: Reads[tweetIdForm] = new Reads[tweetIdForm] {
    override def reads(json: JsValue): JsResult[tweetIdForm] = {
      JsSuccess(tweetIdForm(
        tweetId = (json \ "tweet_id").as[Int],
        evalStatus = (json \ "eval_status").as[Int]
      )
      )
    }
  }
}