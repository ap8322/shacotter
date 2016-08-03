package models

import javax.inject.Inject

import com.google.inject.ImplementedBy
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
  * Created by yuki.haneda on 2016/08/03.
  */
@ImplementedBy(classOf[tweetDAO])
trait tweetDAOWrap extends HasDatabaseConfigProvider[JdbcProfile]{

}

class tweetDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with tweetDAOWrap{

}
