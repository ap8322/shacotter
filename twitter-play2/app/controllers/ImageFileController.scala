package controllers

import java.io.File
import java.nio.file.{Files, Path}
import java.util.Base64
import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.auth.AuthConfigImpl
import models.dao.{ImageDAO, MemberDAO}
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by yuki.haneda on 2016/08/30.
  */
class ImageFileController @Inject()(val memberDAO: MemberDAO,
                                    val imageDAO: ImageDAO,
                                    val cacheApi: CacheApi)
  extends Controller with AuthElement with AuthConfigImpl {

  def upload = AsyncStack(parse.multipartFormData, AuthorityKey -> None) { implicit request =>
    request.body.file("picture").map { picture =>
      val id = loggedIn.memberId
      val imageFile = picture.ref.file
      val imageName = picture.filename

      imageDAO.fetch(id).map {
        case Some(_) => imageDAO.update(id, imageName, imageToBase64(imageFile))
        case _ => imageDAO.insert(id, imageName, imageToBase64(imageFile))
      }.map(_ =>
        Redirect(routes.TweetController.timeline).flashing(
          "message" -> "アイコンが変更されました｡"
        ))
    }.getOrElse {
      Future {
        Redirect(routes.MemberController.edit).flashing(
          "message" -> "アイコンの変更に失敗しました｡"
        )
      }
    }
  }

  def download(id: Long) = Action.async { implicit rs =>
    imageDAO.fetch(id).map {
      case Some(image) => Ok.sendFile(
        base64ToImage(image.imageName, image.imageData)
      ).withHeaders(CONTENT_TYPE -> "application/force-download")
      case _ => BadRequest
    }
  }

  /**
    * image -> base64
    *
    * @param image posted image
    * @return
    */
  private[this] def imageToBase64(image: File): String = {
    val bytes: Array[Byte] = Files.readAllBytes(image.toPath)
    Base64.getEncoder.encodeToString(bytes)
  }

  /**
    * base64 -> image
    *
    * @param imageName
    * @param base64Text
    * @return
    */
  private[this] def base64ToImage(imageName: String, base64Text: String): File = {
    val bytes: Array[Byte] = Base64.getDecoder.decode(base64Text)
    val path: Path = new File(s"/tmp/$imageName").toPath
    Files.write(path, bytes).toFile
  }
}