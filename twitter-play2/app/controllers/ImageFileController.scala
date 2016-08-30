package controllers

import java.io.File
import java.nio.file.{Files, Path}
import java.sql.Blob
import java.util.Base64
import javax.inject.Inject
import javax.sql.rowset.serial.SerialBlob

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{Action, Controller}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by yuki.haneda on 2016/08/30.
  */
class ImageFileController @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with Controller {

  def upload = Action.async(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      val imagefile = picture.ref.file.toPath
      val filename = picture.filename

      //imageファイルをbyte配列 ー> Blobの変換
      val bytes: Array[Byte] = Files.readAllBytes(imagefile)
      val blob: Blob = new SerialBlob(bytes)

      bytes.foreach(print)
      println(blob.length)

      case class imageInfo(imageData: Blob, imageDataLength: Long)

      //dbに格納(image名,blob,length)
      val encode: Future[String] = for {
      //_ <- db.run(Image += ImageRow(1, 1, filename, blob, blob.length))
        image <- db.run(Image.filter(_.imageId === 1.toLong).result).map(_.map(i => imageInfo(i.imageData, i.imageDataLength)).head)
      } yield {
        // blob -> byte配列
        val path: Path = new File(s"/tmp/picture/$filename").toPath
        val DBBytes: Array[Byte] = image.imageData.getBytes(1, image.imageDataLength.toInt)

        //Files.write(path, DBBytes)
        Base64.getEncoder.encodeToString(DBBytes)
      }
      encode.map(e => Ok(e))
    }.getOrElse {
      Future {
        Redirect(routes.MemberController.edit).flashing(
          "error" -> "Missing file"
        )
      }
    }
  }
}
