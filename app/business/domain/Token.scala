package business.domain

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._
import persistence.DBManager._

/**
 * Database object for Token - should never be sent to client directly
 * @param _id primary key (sent to client)
 * @param userId associated user's id (not sent to client)
 */
case class Token(_id: String, userId: String) {

  private[business] def save(): Token = withDB { db =>
    val tokensCollection = db("tokens")
    tokensCollection.update(getQuery, grater[Token].asDBObject(this), upsert = true)
    this
  }

  private def getQuery: DBObject = {
    MongoDBObject("_id" -> _id)
  }
}

/**
 * Companion object for Token
 */
object Token extends Collected {

  override def collection = "tokens"

  private[business] def getByID(_id: String): Option[Token] = withCollection { collection =>
    val criteria = MongoDBObject("_id" -> _id)
    val cursor = collection.findOne(criteria)

    cursor.map { x =>
      Option(grater[Token].asObject(x))
    }.getOrElse(None)
  }

  private[business] def getByUserId(userId: String): Option[Token] = withCollection { collection =>
    val criteria = MongoDBObject("userId" -> userId)
    val cursor = collection.findOne(criteria)

    cursor.map { x =>
      Option(grater[Token].asObject(x))
    }.getOrElse(None)
  }

  def getUserFromToken(_id: String): Option[User] = {
    val userToken = getByID(_id)
    userToken.flatMap { tkn =>
      User.getByID(tkn.userId)
    }
  }

  def withUser[T](_id: String)(block: User => T): Option[T] = {
    getUserFromToken(_id).map { usr =>
      block(usr)
    }
  }

  def isTokenValid(_id: String): Boolean = {
    getUserFromToken(_id).isDefined
  }

  def deleteById(_id: String): Unit = withCollection { collection =>
    collection.findAndRemove(MongoDBObject("_id" -> _id))
  }
}