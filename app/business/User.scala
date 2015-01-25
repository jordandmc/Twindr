package business

import com.mongodb.casbah.Imports._
import persistence.DBManager._
import com.novus.salat._
import com.novus.salat.global._

case class User(id: String, oauthToken: String) {

  private[business] def save(): Unit = withDB { db =>
    val usersCollection = db("users")
    usersCollection.update(getQuery, grater[User].asDBObject(this), upsert = true)
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> id)
  }
}

object User {

  private[business] def getByID(id: String): Option[User] = withDB { db =>
    val usersCollection = db("users")
    val criteria = MongoDBObject("_id" -> id)
    val cursor = usersCollection.findOne(criteria)

    cursor.map { x =>
      Option(grater[User].asObject(x))
    }.getOrElse(None)
  }
}

