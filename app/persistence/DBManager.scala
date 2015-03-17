package persistence

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Play

object DBManager {

  type DB = MongoDB

  //TODO: Add authentication to database
  private val mongoClient = MongoClient("localhost", 27017)
  private def getDB = mongoClient(Play.current.configuration.getString("mongodb.db").getOrElse("db"))

  def withDB[A](block: DB => A): A = {
    block(getDB)
  }

  def createIndexes(): Unit = withDB { db =>
    val usersCollection = db.getCollection("users")

    usersCollection.createIndex(MongoDBObject("random" -> 1))
    usersCollection.createIndex(MongoDBObject("location" -> "2dsphere"))
  }
}
