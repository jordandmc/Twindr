package business.logic

import java.util.{Date, UUID}

import business.domain.{User, Token}
import com.mongodb.casbah.commons.MongoDBObject
import persistence.DBManager._
import play.api.libs.oauth.RequestToken
import play.api.mvc.RequestHeader

object Register {


  def register(oauthToken: RequestToken, male:Boolean, dateOfBirth: Date, location: String, interests: String): Token = {
    val twitterName = "@callwebservice"

    val user = User(UUID.randomUUID().toString, oauthToken, twitterName, male, dateOfBirth, location, interests).save()

    withDB { db =>
      val tokensCollection = db("tokens")
      tokensCollection.findAndRemove(MongoDBObject("userId" -> user._id))

      Token(UUID.randomUUID().toString, user._id).save()
    }
  }
}
