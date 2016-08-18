package controllers


import com.google.inject.Inject
import controllers.EvalController.tweetIdForm
import jp.t2v.lab.play2.auth.AuthElement
import models.Tables._
import models._
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yuki.haneda on 2016/08/18.
  */
class EvalController @Inject()(val memberDAO: MemberDAO,
                               val tweetDAO: TweetDAO,
                               val evalDAO: EvalDAO)
  extends Controller with AuthElement with AuthConfigImpl {

  /**
    * iine!
    *
    * @return
    */
  def good() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[tweetIdForm].map { form =>
      //そのコメント(tweetId)に対して､その人(loggedIn.id)がいいねした事を登録
      val eval = EvalRow(form.tweetId, form.evalStatus, loggedIn.memberId)
      evalDAO.insertStatus(eval)
      Future.successful(Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus)))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * None
    *
    * @return
    */
  def deleteEval() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[tweetIdForm].map { form =>
      evalDAO.deleteStatus(form.tweetId, loggedIn.memberId)
      Future.successful(Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus)))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * doudemoiine!
    *
    * @return
    */
  def bad() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[tweetIdForm].map { form =>
      //そのコメント(tweetId)に対して､その人(loggedIn.id)がいいねした事を登録
      val eval = EvalRow(form.tweetId, 0, loggedIn.memberId)
      evalDAO.insertStatus(eval)
      Future.successful(Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus)))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  /**
    * update status
    *
    * @return
    */
  def updateStatus() = AsyncStack(parse.json, AuthorityKey -> None) { implicit rs =>
    rs.body.validate[tweetIdForm].map { form =>
      //そのコメント(tweetId)に対して､その人(loggedIn.id)がいいねした事を登録
      val eval = EvalRow(form.tweetId, form.evalStatus, loggedIn.memberId)
      evalDAO.updateStatus(eval)
      Future.successful(Ok(Json.obj("result" -> form.tweetId, "eval_status" -> form.evalStatus)))
    }.recoverTotal { e =>
      Future {
        Ok(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
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