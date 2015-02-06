package business.domain

import java.util.Date

import com.mongodb.casbah.Imports._
import com.novus.salat._

/**
 * Match database object - should never be sent client side
 * @param _id the primary key
 * @param user1 the first user
 * @param user2 the second user
 * @param unmatched true iff one of the users has elected to unmatch for this match
 */
case class Match(_id: String, user1: String, user2: String, unmatched: Boolean = false) {

  private[business] def save(): Match = Match.withCollection { collection =>
    collection.update(getQuery, grater[Match].asDBObject(this), upsert = true)
    this
  }

  private[business] def unmatch(): Match = {
    copy(unmatched = true)
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> _id)
  }
}

/**
 * Companion object for Match
 */
object Match extends Collected {
  def collection = "matches"

  private[business] def getMatchesForUser(user: User): List[Match] = Match.withCollection { collection =>
    val criteria = $and(
      $or(
        "user1" $eq user._id,
        "user2" $eq user._id
      ),
      "unmatched" $eq false
    )

    collection.find(criteria).toList.map { dbObj =>
      grater[Match].asObject(dbObj)
    }
  }

  private[business] def getMatchByUsers(user1: User, user2: User): Option[Match] = Match.withCollection { collection =>
    val criteria = $or(
      $and("user1" $eq user1._id, "user2" $eq user2._id),
      $and("user1" $eq user2._id, "user2" $eq user1._id)
    )

    collection.findOne(criteria).map{ x =>
      grater[Match].asObject(x)
    }
  }
}

/**
 * Data transfer object to send existing matches to the client
 * @param username the other user's twitter username
 * @param sex the other user's sex
 * @param dateOfBirth the other user's date of birth
 */
case class PreparedMatch(username: String, sex: String, dateOfBirth: Date) {

}