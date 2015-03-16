package business.domain

import java.util.{UUID, Date}

import com.mongodb.casbah.query.Imports._
import com.novus.salat._

/**
 * Stores a message sent between a matched user A and another matched user B
 * @param _id The primary key
 * @param matchID The unique match identifier
 * @param sender Of the two matchees, who sent the message
 * @param message The message contents
 * @param dateTime The time the message was sent
 */
case class MatchMessage(_id: String, matchID: String, sender: String, message: String, dateTime: Date) {

  private[business] def save(): MatchMessage = MatchMessage.withCollection { collection =>
    collection.update(getQuery, grater[MatchMessage].asDBObject(this), upsert = true)
    this
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> _id)
  }
}

object MatchMessage extends Collected {

  override def collection = "messages"

  def processNewMessage(message: MatchMessage): Unit = {
    val msg = message.copy(_id = UUID.randomUUID().toString)
    msg.save()
  }

}
