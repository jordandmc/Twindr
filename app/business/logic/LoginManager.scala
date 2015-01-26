package business.logic

import java.util.UUID
import com.mongodb.casbah.commons.MongoDBObject
import persistence.DBManager._

import business.domain.{Token, User}

object LoginManager {

  def login(oauthToken: String): Token = {
    val twitterName = "@callwebservice"
    val user = User.getByTwitterName(twitterName) match {
      case Some(u: User) => u
      case None => User(UUID.randomUUID().toString, oauthToken, twitterName).save()
    }

    withDB { db =>
      val tokensCollection = db("tokens")
      tokensCollection.findAndRemove(MongoDBObject("userId" -> user._id))

      Token(UUID.randomUUID().toString, user._id).save()
    }
  }
}
