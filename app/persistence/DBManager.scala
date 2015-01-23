package persistence

import com.mongodb.casbah.Imports._

object DBManager {

  type DB = MongoDB

  private val mongoClient = MongoClient("localhost", 27017)
  private def getDB = mongoClient("db")

  def withDB[A](block: DB => A): A = {
    block(getDB)
  }
}
