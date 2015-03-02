package controllers

import business.domain.{Token, User}
import business.logic.RegistrationManager
import play.api.Logger
import play.api.libs.oauth.RequestToken
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future

class AuthenticatedRequest[A](val user: User, val request: Request[A], val token: Token) extends WrappedRequest[A](request)

object AuthAction extends ActionBuilder[AuthenticatedRequest] {

  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {

    val tokenId = request.headers.get("X-Auth-Token")

    val (token, web) = tokenId match {
      case tkn: Some[String] => (tkn, false)
      case _ =>
        request.session.get("X-Auth-Token") match {
          case tkn: Some[String] => (tkn, true)
          case _ => (None, false)
        }
    }

    val result = token match {
      case Some(tkn: String) => Token.withUser(tkn) { usr =>
        val webNotRegistered = web match {
          case true => ! RegistrationManager.hasRegistered(usr)
          case _ => false
        }

        webNotRegistered match {
          case true if ! request.path.contains("register") && ! request.path.contains("logout") =>
            Future.successful(Redirect(routes.Login.register()))
          case _ => block(new AuthenticatedRequest[A](usr, request, Token(tkn, usr._id)))
        }

      }
      case _ => Future.successful(Redirect(routes.Application.index()))
    }

    result match {
      case None => Future.successful(Redirect(routes.Application.index()))
      case Some(res: Future[Result]) => res
      case _ => Future.successful(Redirect(routes.Application.index()))
    }
  }

  def isAuthenticated[A](request: RequestHeader): Boolean = {
    val tokenId = request.headers.get("X-Auth-Token")

    val token = tokenId match {
      case tkn: Some[String] => tkn
      case _ =>
        request.session.get("X-Auth-Token") match {
          case tkn: Some[String] => tkn
          case _ => None
        }
    }

    token match {
      case Some(tkn: String) => Token.isTokenValid(tkn)
      case _ => false
    }
  }

  def getUserFromRequest[A](request: Request[A]): Option[User] = {
    val tokenId = request.headers.get("X-Auth-Token")

    val token = tokenId match {
      case tkn: Some[String] => tkn
      case _ =>
        request.session.get("X-Auth-Token") match {
          case tkn: Some[String] => tkn
          case _ => None
        }
    }

    token match {
      case Some(tkn: String) => Token.getUserFromToken(tkn)
      case _ => None
    }
  }

  def getTokenString[A](request: AuthenticatedRequest[A]): Option[String] = {
    val tokenId = request.headers.get("X-Auth-Token")

    tokenId match {
      case tkn: Some[String] => tkn
      case _ =>
        request.session.get("X-Auth-Token") match {
          case tkn: Some[String] => tkn
          case _ => None
        }
    }
  }

  def getUserOAuthToken[A](request: AuthenticatedRequest[A]): Option[RequestToken] = {
    Option(request.user.oauthToken)
  }
}