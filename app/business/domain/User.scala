package business.domain

import com.mongodb.casbah.Imports._
import com.novus.salat._
import persistence.DBManager._
import play.api.libs.oauth.RequestToken
import java.util.Date

case class User(_id: String, oauthToken: RequestToken, twitterName: String,
                sex:Option[String], dateOfBirth: Option[Date], location: Option[String], interests: List[String]) {
  private[business] def save(): User = withDB { db =>
    val usersCollection = db("users")
    usersCollection.update(getQuery, grater[User].asDBObject(this), upsert = true)
    this
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> _id)
  }
}


object User {

  private[business] def getByID(_id: String): Option[User] = withDB { db =>
    val usersCollection = db("users")
    val criteria = MongoDBObject("_id" -> _id)
    val cursor = usersCollection.findOne(criteria)

    cursor.map { x =>
      Option(grater[User].asObject(x))
    }.getOrElse(None)
  }

  private[business] def getByTwitterName(twitterName: String): Option[User] = withDB { db =>
    val usersCollection = db("users")
    val criteria = MongoDBObject("twitterName" -> twitterName)
    val cursor = usersCollection.findOne(criteria)

    cursor.map { x =>
      Option(grater[User].asObject(x))
    }.getOrElse(None)
  }
}

