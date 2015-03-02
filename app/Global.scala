import com.mongodb.casbah.commons.MongoDBObject
import play.api._
import persistence.DBManager._

object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = withDB { db =>
    val usersCollection = db.getCollection("users")

    usersCollection.createIndex(MongoDBObject("random" -> 1))
    usersCollection.createIndex(MongoDBObject("location" -> "2dsphere"))
  }

  override def onStop(app: Application): Unit = {

  }
}
