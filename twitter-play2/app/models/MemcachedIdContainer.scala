package models

/**
  * Created by yuki.haneda on 2016/08/22.
  */
import play.api.cache._
import play.api.Play._

import scala.annotation.tailrec
import scala.util.Random
import java.security.SecureRandom
import javax.inject.Inject

import com.google.inject.ImplementedBy
import jp.t2v.lab.play2.auth.{AsyncIdContainer, AuthenticityToken, IdContainer}

import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

@ImplementedBy(classOf[MemcachedIdContainerImpl[Int]])
trait MemcachedIdContainer [Id] extends IdContainer[Id]{

  def startNewSession(userId: Id, timeoutInSeconds: Int): AuthenticityToken

  def remove(token: AuthenticityToken)

  def get(token: AuthenticityToken): Option[Id]

  def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int)
}

class MemcachedIdContainerImpl [Id: ClassTag] @Inject() (cache: CacheApi) extends IdContainer[Id] with MemcachedIdContainer[Id] {

  val tokenSuffix = ":token"
  val userIdSuffix = ":userId"
  val random = new Random(new SecureRandom())

  def startNewSession(userId: Id, timeoutInSeconds: Int): AuthenticityToken = {
    removeByUserId(userId)
    val token = generate
    store(token, userId, timeoutInSeconds)
    token
  }

  @tailrec
  final def generate: AuthenticityToken = {
    val table = "abcdefghijklmnopqrstuvwxyz1234567890_.~*'()"
    val token = Iterator.continually(random.nextInt(table.size)).map(table).take(64).mkString
    if (get(token).isDefined) generate else token
  }

  private def removeByUserId(userId: Id) {
    cache.get[String](userId.toString + userIdSuffix) foreach unsetToken
    unsetUserId(userId)
  }

  def remove(token: AuthenticityToken) {
    get(token) foreach unsetUserId
    unsetToken(token)
  }

  private def unsetToken(token: AuthenticityToken) {
    cache.remove(token + tokenSuffix)
  }

  private def unsetUserId(userId: Id) {
    cache.remove(userId.toString + userIdSuffix)
  }

  def get(token: AuthenticityToken) = cache.get(token + tokenSuffix).map(_.asInstanceOf[Id])

  private def store(token: AuthenticityToken, userId: Id, timeoutInSeconds: Int) {
    cache.set(token + tokenSuffix, userId, Duration.fromNanos(timeoutInSeconds))
    cache.set(userId.toString + userIdSuffix, token, Duration.fromNanos(timeoutInSeconds))
  }

  def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int) {
    get(token).foreach(store(token, _, timeoutInSeconds))
  }

}