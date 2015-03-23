package business.logic

import java.util.UUID

import business.domain.{Token, User}
import controllers.TwitterProvider
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
    login(twitterName, oauthToken)
  }

  /**
   * Execute login logic for mobile login
   * @param twitterName the screen name of the Twitter user
   * @return a new authentication token for authenticated web-services and pages
   */
  def mobileLogin(twitterName: String): Token ={
    login(twitterName, null)
  }

  def logout(token: Token): Unit = {
    token.delete()
  }

  /**
   * Execute login logic
   * @param twitterName the screen name of the Twitter user
   * @return a new authentication token for authenticated web-services and pages
   */
  private def login(twitterName: String, oauthToken: RequestToken): Token ={
    val user = User.getByTwitterName(twitterName) match {
      case Some(u: User) => u
      case None =>
        User(UUID.randomUUID().toString, oauthToken, twitterName, None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble()).save()
    }

    Token(UUID.randomUUID().toString, user._id).save()
  }
}