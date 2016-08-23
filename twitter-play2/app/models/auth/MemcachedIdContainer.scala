package models.auth

/**
  * Created by yuki.haneda on 2016/08/22.
  */

import java.security.SecureRandom

import jp.t2v.lab.play2.auth.{AuthenticityToken, IdContainer}
import play.api.cache.CacheApi

import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.reflect.ClassTag
import scala.util.Random

class MemcachedIdContainer[Id: ClassTag](cacheApi: CacheApi) extends IdContainer[Id] {

  private val tokenSuffix = ":token"
  private val userIdSuffix = ":userId"
  private val random = new Random(new SecureRandom())

  private def intToDuration(seconds: Int): Duration = if (seconds == 0) Duration.Inf else seconds.seconds

  def startNewSession(userId: Id, timeoutInSeconds: Int): AuthenticityToken = {
    removeByUserId(userId)
    val token = generate
    store(token, userId, timeoutInSeconds)
    token
  }

  @tailrec
  private final def generate: AuthenticityToken = {
    val table = "abcdefghijklmnopqrstuvwxyz1234567890_.~*'()"
    val token = Iterator.continually(random.nextInt(table.size)).map(table).take(64).mkString
    if (get(token).isDefined) generate else token
  }

  private def removeByUserId(userId: Id) {
    cacheApi.get[String](userId.toString + userIdSuffix) foreach unsetToken
    unsetUserId(userId)
  }

  def remove(token: AuthenticityToken) {
    get(token) foreach unsetUserId
    unsetToken(token)
  }

  private def unsetToken(token: AuthenticityToken) {
    cacheApi.remove(token + tokenSuffix)
  }

  private def unsetUserId(userId: Id) {
    cacheApi.remove(userId.toString + userIdSuffix)
  }

  def get(token: AuthenticityToken) = cacheApi.get[Any](token + tokenSuffix).map(_.asInstanceOf[Id])

  private def store(token: AuthenticityToken, userId: Id, timeoutInSeconds: Int) {
    cacheApi.set(token + tokenSuffix, userId, intToDuration(timeoutInSeconds))
    cacheApi.set(userId.toString + userIdSuffix, token, intToDuration(timeoutInSeconds))
  }

  def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int) {
    get(token).foreach(store(token, _, timeoutInSeconds))
  }
}