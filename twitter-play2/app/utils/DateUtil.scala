package utils

/**
  * Created by yuki.haneda on 2016/08/29.
  */
object DateUtil extends DateUtil

trait DateUtil extends SystemClock {
  def now = currentTimestamp
}