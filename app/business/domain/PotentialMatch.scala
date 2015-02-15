package business.domain

import java.util.Date

import business.logic.PotentialMatchGenerator
import com.mongodb.casbah.Imports._
import com.novus.salat._

/**
 * Potential match database object - should never be sent client side
 * @param _id primary key
 * @param user1 the first user's id
 * @param user2 the second user's id
 * @param user1State the acceptance status of the first user
 * @param user2State the acceptance status of the second user
 */
case class PotentialMatch(_id: String, user1: String, user2: String, user1State: String = PotentialMatch.PENDING, user2State: String = PotentialMatch.PENDING) {

  private[business] def save(): PotentialMatch = PotentialMatch.withCollection { collection =>
    collection.update(getQuery, grater[PotentialMatch].asDBObject(this), upsert = true)
    this
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> _id)
  }

  private[business] def accept(user: User): PotentialMatch = {
    changeState(user, PotentialMatch.ACCEPTED)
  }

  private[business] def reject(user: User): PotentialMatch = {
    changeState(user, PotentialMatch.REJECTED)
  }

  private def changeState(user: User, state: String): PotentialMatch = {
    if(user._id == user1) {
      copy(user1State = state)
    } else {
      copy(user2State = state)
    }
  }
}

/**
 * Companion object for PotentialMatch
 */
object PotentialMatch extends Collected {
  final val PENDING = "PENDING"
  final val ACCEPTED = "ACCEPTED"
  final val REJECTED = "REJECTED"

  override def collection = "potentialmatches"

  private[business] def getByID(_id: String): Option[PotentialMatch] = withCollection { collection =>
    val criteria = MongoDBObject("_id" -> _id)
    val cursor = collection.findOne(criteria)

    cursor.map { x =>
      Option(grater[PotentialMatch].asObject(x))
    }.getOrElse(None)
  }

  private[business] def getPendingForUser(user: User): List[PotentialMatch] = withCollection { collection =>

    val criteria = $or(
      $and( "user1" $eq user._id, $nor("user1State" $eq REJECTED, "user1State" $eq ACCEPTED, "user2State" $eq REJECTED)),
      $and( "user2" $eq user._id, $nor("user2State" $eq REJECTED, "user2State" $eq ACCEPTED, "user1State" $eq REJECTED))
    )

    collection.find(criteria).limit(PotentialMatchGenerator.POTENTIAL_MAX).toList.map { dbObj =>
      grater[PotentialMatch].asObject(dbObj)
    }
  }

  private[business] def getForUserAndMatchResponse(user: User, response: PotentialMatchResponse): Option[PotentialMatch] = withCollection { collection =>
    val otherUser = User.getByTwitterName(response.username)
    otherUser.flatMap { other =>
      getForUsers(user, other)
    }
  }

  private[business] def getForUsers(user1: User, user2: User): Option[PotentialMatch] = withCollection { collection =>
    val criteria = $or(
      $and("user1" $eq user1._id, "user2" $eq user2._id),
      $and("user1" $eq user2._id, "user2" $eq user1._id)
    )

    collection.findOne(criteria).map { x =>
      grater[PotentialMatch].asObject(x)
    }
  }
}

/**
 * Data transfer object to send the client the information it needs to display a potential match
 * @param username the other user's twitter username
 * @param tweets the other users most recent non-reply tweets
 * @param sex the sex of the other user
 * @param dateOfBirth the date of birth of the other user
 */
case class PreparedPotentialMatch(username: String, tweets: List[String], sex: String, dateOfBirth: Date) {

}

/**
 * Data transfer object used to deserialize the potential match response received from the client
 * @param username the other user's twitter username
 * @param status the actual response (should never be PENDING)
 */
case class PotentialMatchResponse(username: String, status: String) {

}


