package controllers

import business.domain.{MobileLoginResponse, Token}
import business.logic.{RegistrationManager, LoginManager}
import play.api.Play
import play.api.Play.current
import play.api.libs.oauth._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller, RequestHeader}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object TwitterProvider extends Controller {

  val KEY = ConsumerKey("GDhLcLnWs0V6xoWDRa7w4D1Qu", "xWGqSefoHfa6FPVTPGMYobCTkPyAzaXxNcUyfTMHhSeUReMhXR")

  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authenticate", KEY),
    use10a = true)

  def authenticate = Action { request =>
    request.queryString.get("oauth_verifier").flatMap(_.headOption).map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(accessToken) =>
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Login.login()).withSession("X-Auth-Token" -> LoginManager.login(accessToken)._id)
        case Left(e) => throw e
      }
    }.getOrElse(
        TWITTER.retrieveRequestToken(Play.current.configuration.getString("oauthCallback", Option(Set("", "http://localhost:9000/auth"))).getOrElse("")) match {
          case Right(t) =>
            // We received the unauthorized tokens in the OAuth object - store it before we proceed
            Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
          case Left(e) => throw e
        })
  }

  def mobileLogin = Action { request =>
    request.body.asJson match {
      case Some(jsonRequest) =>
        jsonRequest.asOpt[RequestToken] match {
          case Some(accessToken) =>
            val token = LoginManager.login(accessToken)
            val  hasRegistered = Token.getUserFromToken(token._id) match {
              case Some(user) =>
                RegistrationManager.hasRegistered(user)
              case _ =>
                false
            }
            val loginResponse = MobileLoginResponse(token._id, hasRegistered)
            Ok(Json.toJson(loginResponse))
          case None =>
            BadRequest("Expects a MobileLoginRequest.")
        }
      case _ =>
        BadRequest("No body sent.")
    }
  }

  private def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }

  def timeline[A](request: AuthenticatedRequest[A]): List[String] = {
    AuthAction.getUserOAuthToken(request) match {
      case Some(sessionToken) =>
        val result =  WS.url("https://api.twitter.com/1.1/statuses/user_timeline.json?count=20")
            .sign(OAuthCalculator(TwitterProvider.KEY, sessionToken))
            .get()

        val resultFuture = result.map { response =>
          (response.json \\ "text").map(_.as[String]).toList
        } recover {
          case timeout: java.util.concurrent.TimeoutException => List[String]()
        }

        Await.result(resultFuture, 5000 millis)
      case _ => List[String]()
    }
  }

  def getTwitterName(oauthToken: RequestToken): Option[String] = {

    val result = WS.url("https://api.twitter.com/1.1/account/verify_credentials.json")
      .sign(OAuthCalculator(TwitterProvider.KEY, oauthToken))
      .get()

    val resultFuture = result.map{ response =>
      Option((response.json \ "screen_name").as[String])
    } recover {
      case timeout: java.util.concurrent.TimeoutException => None
    }

    Await.result(resultFuture, 5000 millis)
  }
}
