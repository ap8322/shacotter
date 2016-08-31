package controllers

import java.io.File
import java.nio.file.Files
import java.util.Base64
import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import models.auth.AuthConfigImpl
import models.dao.{ImageDAO, MemberDAO}
import play.api.cache.CacheApi
import play.api.mvc.Controller

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
      val imageFile = picture.ref.file
      val filename = picture.filename

      imageDAO.insert(loggedIn.memberId, filename, imageToBase64(imageFile)).map(_ =>
        Redirect(routes.MemberController.edit).flashing(
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

}

//val blob: Blob = new SerialBlob(bytes)
//dbに格納(image名,blob,length)
//      val encode: Future[String] = for {
//      //_ <- db.run(Image += ImageRow(1, 1, filename, blob, blob.length))
//        image <- db.run(Image.filter(_.imageId === 1.toLong).result).map(_.map(i => imageInfo(i.imageData, i.imageDataLength)).head)
//      } yield {
//        // blob -> byte配列
//        val path: Path = new File(s"/tmp/picture/$filename").toPath
//        val DBBytes: Array[Byte] = image.imageData.getBytes(1, image.imageDataLength.toInt)
//
//        //Files.write(path, DBBytes)
//        Base64.getEncoder.encodeToString(DBBytes)
//      }
//      encode.map(e => Ok(e))

