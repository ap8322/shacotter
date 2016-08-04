package controllers

import java.util.Date

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.db.slick._
import slick.driver.JdbcProfile
import models.Tables._
import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.{AuthConfigImpl, MemberDAOLike, Tables}

import scala.concurrent.Future
import slick.driver.MySQLDriver.api._

object UserController {

  // フォームの値を格納するケースクラス
  case class MemberForm(id: Option[Int], name: String)

  // formから送信されたデータ ⇔ ケースクラスの変換
  val memberForm = Form(
    mapping(
      "id" -> optional(number),
      "name" -> nonEmptyText(maxLength = 20)
    )(MemberForm.apply)(MemberForm.unapply)
  )
}

class UserController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                               val messagesApi: MessagesApi,
                               val memberDAO: MemberDAOLike) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport with AuthElement with AuthConfigImpl {
  /**
    * 一覧表示
    */
  def list = TODO

  /**
    * 編集画面表示
    */
  def edit(id: Option[Long]) = TODO

  /**
    * 登録実行
    */
  def create = TODO

  /**
    * 更新実行
    */
  def update = TODO

  /**
    * 削除実行
    */
  def remove(id: Long) = TODO

//  /**
//    * 編集画面表示
//    */
//  // コンパニオンオブジェクトに定義したFormを参照するためにimport文を追加
//  import UserController._
//
//  def edit(id: Option[Int]) = Action.async { implicit rs =>
//    val form: Future[Form[MemberForm]] = if (id.isDefined) {
//      // IDからユーザ情報を1件取得
//      db.run(Member.filter(t => t.id === id.get.bind).result.head).map { user =>
//        // 値をフォームに詰める
//        memberForm.fill(MemberForm(Some(user.id), user.name))
//      }
//    } else {
//      // リクエストパラメータにIDが存在しない場合
//      Future {
//        memberForm
//      }
//    }
//
//    form.map { form =>
//      Ok(views.html.user.edit(form))
//    }
//  }
//
//  /**
//    * 登録実行
//    */
//  def create = Action.async { implicit rs =>
//    memberForm.bindFromRequest.fold(
//      error => {
//        Future(BadRequest(views.html.user.edit(error)))
//      },
//      form => {
//        // ユーザを登録
//        val member = MemberRow(0, form.name, "hoge", "FML", None, None, new java.sql.Timestamp(new Date().getTime), "hoge", new java.sql.Timestamp(new Date().getTime), "hoge", 1)
//        db.run(Member += member).map { _ =>
//          // 一覧画面へリダイレクト
//          Redirect(routes.UserController.list)
//        }
//      }
//    )
//  }
//
//  /**
//    * 更新実行
//    */
//  def update = Action.async { implicit rs =>
//    // リクエストの内容をバインド
//    memberForm.bindFromRequest.fold(
//      error => {
//        Future(BadRequest(views.html.user.edit(error)))
//      },
//
//      member => {
//        // ユーザ情報を更新
//        db.run(Member.filter(t => t.id === member.id.bind).result.head).map { m =>
//          val upmember = MemberRow(member.id.get, member.name, m.memberAccount,
//            m.memberStatusCode, m.formalizedDatetime, m.birthdate, m.registerDatetime,
//            m.registerUser, m.updateDatetime, m.updateUser, m.versionNo)
//
//          db.run(Member.filter(t => t.id === member.id.bind).update(upmember))
//        }.map { _ =>
//          Redirect(routes.UserController.list)
//        }
//      }
//    )
//  }
//
//  /**
//    * 削除実行
//    */
//  def remove(id: Int) = Action.async { implicit rs =>
//    // ユーザを削除
//    db.run(Member.filter(t => t.id === id.bind).delete).map { _ =>
//      // 一覧画面へリダイレクト
//      Redirect(routes.UserController.list)
//    }
//  }
}