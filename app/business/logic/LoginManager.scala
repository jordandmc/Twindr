package business.logic

import java.util.UUID
import com.mongodb.casbah.commons.MongoDBObject
import persistence.DBManager._

import business.domain.{Token, User}
import play.api.libs.oauth.RequestToken
import play.api.mvc.RequestHeader

object LoginManager {

  def login(oauthToken: RequestToken): Token = {
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

  def getSessionToken(implicit request:RequestHeader): Option[RequestToken] = {
      request.session.get("id") match{
        case Some(id) => Token.getUserFromToken(id) match {
          case Some(user) => Option(user.oauthToken)
          case _ => None
        }
        case _ => None
      }
  }
}
