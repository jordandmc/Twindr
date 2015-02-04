package controllers

import business.logic.LoginManager
import play.api.libs.oauth._
import play.api.libs.ws.WS
import play.api.mvc.{RequestHeader, Action, Controller}
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TwitterProvider extends Controller {

  val KEY = ConsumerKey("GDhLcLnWs0V6xoWDRa7w4D1Qu", "xWGqSefoHfa6FPVTPGMYobCTkPyAzaXxNcUyfTMHhSeUReMhXR")

  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    use10a = true)

  def authenticate = Action { request =>
    request.queryString.get("oauth_verifier").flatMap(_.headOption).map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(accessToken) =>
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Application.index()).withSession("id" -> LoginManager.login(accessToken)._id)
        case Left(e) => throw e
      }
    }.getOrElse(
        TWITTER.retrieveRequestToken("http://localhost:9000/auth") match {
          case Right(t) =>
            // We received the unauthorized tokens in the OAuth object - store it before we proceed
            Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
          case Left(e) => throw e
        })
  }

  private def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }

  def timeline = Action.async { implicit request =>
    LoginManager.getSessionToken match {
      case Some(sessionToken) =>
        WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json")
          .sign(OAuthCalculator(TwitterProvider.KEY, sessionToken))
          .get()
          .map(result => Ok(result.json))
      case _ => Future.successful(Redirect(routes.Application.index()))
    }
  }

  def nameLookup = Action.async { implicit request =>
    LoginManager.getSessionToken match {
      case Some(sessionToken) =>
        WS.url("https://api.twitter.com/1.1/account/verify_credentials.json")
          .sign(OAuthCalculator(TwitterProvider.KEY, sessionToken))
          .get()
          .map(result => Ok(result.json))
      case _ => Future.successful(Redirect(routes.Application.index()))
    }
  }
}
