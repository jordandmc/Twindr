package business.domain

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._
import com.novus.salat.global._
import persistence.DBManager._

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

object Token {

  private[business] def getByID(_id: String): Option[Token] = withDB { db =>
    val tokensCollection = db("tokens")
    val criteria = MongoDBObject("_id" -> _id)
    val cursor = tokensCollection.findOne(criteria)

    cursor.map { x =>
      Option(grater[Token].asObject(x))
    }.getOrElse(None)
  }

  private[business] def getByUserId(userId: String): Option[Token] = withDB { db =>
    val tokensCollection = db("tokens")
    val criteria = MongoDBObject("userId" -> userId)
    val cursor = tokensCollection.findOne(criteria)

    cursor.map { x =>
      Option(grater[Token].asObject(x))
    }.getOrElse(None)
  }

  def getUserFromToken(_id: String): Option[User] = withDB { db =>
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

  def deleteById(_id: String): Unit = withDB{ db =>
    val tokensCollection = db("tokens")
    tokensCollection.findAndRemove(MongoDBObject("_id" -> _id))
  }
}