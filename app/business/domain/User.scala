package business.domain

import com.mongodb.casbah.Imports._
import com.novus.salat._
import persistence.DBManager._
import play.api.libs.oauth.RequestToken
import java.util.Date

/**
 * User database object - should never be sent to client
 *
 * @param _id primary key
 * @param oauthToken  Twitter oauth token
 * @param twitterName Twitter username
 * @param sex         User's sex
 * @param dateOfBirth User's date of birth
 * @param location    User's location in geoJSON format
 * @param interests   List of user's interests
 */
case class User(_id: String, oauthToken: RequestToken, twitterName: String,
                sex: Option[String], dateOfBirth: Option[Date], location: Option[String], interests: List[String]) {
  private[business] def save(): User = User.withCollection { collection =>
    collection.update(getQuery, grater[User].asDBObject(this), upsert = true)
    this
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> _id)
  }
}

/**
 * Companion object for User
 */
object User extends Collected {

  override def collection = "users"

  private[business] def getByID(_id: String): Option[User] = User.withCollection { collection =>
    val criteria = MongoDBObject("_id" -> _id)
    val cursor = collection.findOne(criteria)

    cursor.map { x =>
      Option(grater[User].asObject(x))
    }.getOrElse(None)
  }

  private[business] def getByTwitterName(twitterName: String): Option[User] = User.withCollection { collection =>
    val criteria = MongoDBObject("twitterName" -> twitterName)
    val cursor = collection.findOne(criteria)

    cursor.map { x =>
      Option(grater[User].asObject(x))
    }.getOrElse(None)
  }
}

