package utils

import java.sql.Timestamp
import java.util.Date

/**
  * Created by yuki.haneda on 2016/08/29.
  * stanby-atsを参考にさせて頂きました｡
  */
trait SystemClock {
  /**
    * 1970年1月1日（UTC）からの現在日時までの経過時間をミリ秒で返します。
    * このメソッドをオーバーライドすると他のメソッドで返却する日付を変更することができます。
    */
  def currentTimeMills: Long = System.currentTimeMillis()

  /**
    * 現在日時を表すjava.util.Dateオブジェクトを返却します。
    */
  def currentDate: java.util.Date = new Date(currentTimeMills)

  /**
    * 現在日時を表すjava.sql.Timestampオブジェクトを返却します。
    */
  def currentTimestamp: java.sql.Timestamp = new Timestamp(currentTimeMills)
}
