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

import scala.concurrent.Future
import slick.driver.MySQLDriver.api._

object UserController {

  // フォームの値を格納するケースクラス
  case class MemberForm(memberId: Option[Int], memberName: String)

  // formから送信されたデータ ⇔ ケースクラスの変換
  val memberForm = Form(
    mapping(
      "memberId" -> optional(number),
      "memberName" -> nonEmptyText(maxLength = 20)
    )(MemberForm.apply)(MemberForm.unapply)
  )
}

class UserController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                               val messagesApi: MessagesApi) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  /**
    * 一覧表示
    */
  def list = Action.async { implicit rs =>
    // IDの昇順にすべてのユーザ情報を取得
    db.run(Member.sortBy { x => x.memberId }.result).map { users =>
      // 一覧画面を表示
      Ok(views.html.user.list(users))
    }
  }

  /**
    * 編集画面表示
    */
  // コンパニオンオブジェクトに定義したFormを参照するためにimport文を追加
  import UserController._

  def edit(memberId: Option[Int]) = Action.async { implicit rs =>
    // リクエストパラメータにIDが存在する場合
    val form: Future[Form[MemberForm]] = if (memberId.isDefined) {
      // IDからユーザ情報を1件取得
      db.run(Member.filter(t => t.memberId === memberId.get.bind).result.head).map { user =>
        // 値をフォームに詰める
        memberForm.fill(MemberForm(Some(user.memberId), user.memberName))
      }
    } else {
      // リクエストパラメータにIDが存在しない場合
      Future {
        memberForm
      }
    }

    form.map { form =>
      Ok(views.html.user.edit(form))
    }
  }

  /**
    * 登録実行
    */
  def create = Action.async { implicit rs =>
    // リクエストの内容をバインド
    memberForm.bindFromRequest.fold(
      // エラーの場合
      error => {
        Future(BadRequest(views.html.user.edit(error)))
      },
      // OKの場合
      form => {
        // ユーザを登録
        val member = MemberRow(0, form.memberName, "hoge", "FML", None, None, new java.sql.Timestamp(new Date().getTime), "hoge", new java.sql.Timestamp(new Date().getTime), "hoge", 1)
        db.run(Member += member).map { _ =>
          // 一覧画面へリダイレクト
          Redirect(routes.UserController.list)
        }
      }
    )
  }

  /**
    * 更新実行
    */
  def update = Action.async { implicit rs =>
    // リクエストの内容をバインド
    memberForm.bindFromRequest.fold(
      // エラーの場合は登録画面に戻す
      error => {
        Future(BadRequest(views.html.user.edit(error)))
      },
      // OKの場合は登録を行い一覧画面にリダイレクトする
      member => {
        // ユーザ情報を更新
        db.run(Member.filter(t => t.memberId === member.memberId.bind).result.head).map { m =>
          val upmember = MemberRow(member.memberId.get, member.memberName, m.memberAccount,
            m.memberStatusCode, m.formalizedDatetime, m.birthdate, m.registerDatetime,
            m.registerUser, m.updateDatetime, m.updateUser, m.versionNo)

          db.run(Member.filter(t => t.memberId === member.memberId.bind).update(upmember))
        }.map { _ =>
          Redirect(routes.UserController.list)
        }
      }
    )
  }

  /**
    * 削除実行
    */
  def remove(memberId: Int) = Action.async { implicit rs =>
    // ユーザを削除
    db.run(Member.filter(t => t.memberId === memberId.bind).delete).map { _ =>
      // 一覧画面へリダイレクト
      Redirect(routes.UserController.list)
    }
  }
}