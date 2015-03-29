package business.domain

import business.logic.GeoJSONFormatter
import com.mongodb.casbah.query.Imports._
import com.novus.salat._
import play.api.libs.oauth.RequestToken
import java.util.{Calendar, Date}

import scala.util.Random

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
 * @param recentTweets  The most recent tweets pulled from a user (replace when updating)
 * @param random      random value used for taking random samples
 */
case class User(_id: String, oauthToken: RequestToken, twitterName: String,
                sex: Option[String], dateOfBirth: Option[Date], location: Option[DBObject], interests: List[String],
                recentTweets: List[String], random: Double, sexPreference: Option[String] = None) {

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

  def updateUserTweets(user: User, newTweets: List[String]): User = {
    user.copy(recentTweets = newTweets).save()
  }

  def updateUserLocation(user: User, newLocation: Option[DBObject]): User = {
    user.copy(location = newLocation).save()
  }

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

  private[business] def getNear(user: User, maxDistance: Double, maxUsers: Int): List[User] = withCollection { collection =>
    val start = Random.nextDouble()
    val (longitude, latitude) = user.location match {
      case Some(s: DBObject) => GeoJSONFormatter.getCoords(s)
      case _ => (0.0, 0.0)
    }

    val mainCriteria = $and(
      "location" $nearSphere GeoCoords(longitude, latitude),
      "random" $gt start,
      $or("sexPref" $exists false, "sexPref" $eq user.sex.getOrElse("X"))
    )

    val otherCriteria = List(
      user.sexPreference.map { sexPref =>
        "sex" $eq sexPref
      }
    )

    val finalCriteria = otherCriteria.foldLeft(mainCriteria) { (acc: DBObject, current: Option[DBObject]) =>
      current match {
        case Some(criteria: DBObject) =>
          $and(acc, criteria)
        case _ =>
          acc
      }
    }


    val cursor = collection.find(finalCriteria).sort(MongoDBObject("random" -> 1)).limit(maxUsers)

    cursor.toList.map { x =>
      grater[User].asObject(x)
    }
  }
}

