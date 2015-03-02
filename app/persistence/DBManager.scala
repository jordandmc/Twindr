package persistence

import com.mongodb.casbah.Imports._
import play.api.Play

object DBManager {

  type DB = MongoDB

  //TODO: pull this information from application.conf
  //TODO: Add authentication to database
  private val mongoClient = MongoClient("localhost", 27017)
  private def getDB = mongoClient(Play.current.configuration.getString("mongodb.db").getOrElse("db"))

  def withDB[A](block: DB => A): A = {
    block(getDB)
  }
}
