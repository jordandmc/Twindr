package business.logic

import java.util.UUID
import com.mongodb.casbah.commons.MongoDBObject
import controllers.TwitterProvider
import persistence.DBManager._
import play.api.libs.oauth._

import business.domain.{Token, User}
import play.api.libs.oauth.RequestToken
import play.api.libs.ws.WS
import play.api.mvc.RequestHeader

object LoginManager {

  def login(oauthToken: RequestToken): Token = {
    //TODO find a better way to handle the case when getTwitterName returns None
    val twitterName = TwitterProvider.getTwitterName(oauthToken).getOrElse("@" + UUID.randomUUID().toString)
    val user = User.getByTwitterName(twitterName) match {
      case Some(u: User) => u

      case None => User(UUID.randomUUID().toString, oauthToken, twitterName,None,None,None,List()).save()
    }

    withDB { db =>
      val tokensCollection = db("tokens")
      tokensCollection.findAndRemove(MongoDBObject("userId" -> user._id))

      Token(UUID.randomUUID().toString, user._id).save()
    }
  }
}
