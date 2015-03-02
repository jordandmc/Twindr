package business.logic

import java.util.UUID
import com.mongodb.casbah.commons.MongoDBObject
import controllers.TwitterProvider
import persistence.DBManager._
import business.domain.{Token, User}
import play.api.libs.oauth.RequestToken

import scala.util.Random

object LoginManager {

  /**
   * Execute login logic after twitter authentication
   * @param oauthToken the twitter oauth token returned after authentication
   * @return a new authentication token for authenticated web-services and pages
   */
  def login(oauthToken: RequestToken): Token = {
    //TODO find a better way to handle the case when getTwitterName returns None
    val twitterName = TwitterProvider.getTwitterName(oauthToken).getOrElse("@" + UUID.randomUUID().toString)
    val user = User.getByTwitterName(twitterName) match {
      case Some(u: User) => u
      case None =>
        User(UUID.randomUUID().toString, oauthToken, twitterName, None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble()).save()
    }

    Token(UUID.randomUUID().toString, user._id).save()
  }

  def logout(token: Token): Unit = {
    token.delete()
  }
}
