package controllers

import business.domain.{Token, User}
import play.api.libs.oauth.RequestToken
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future

class AuthenticatedRequest[A](val user: User, val request: Request[A]) extends WrappedRequest[A](request)

object AuthAction extends ActionBuilder[AuthenticatedRequest] {

  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {

    val tokenId = request.headers.get("X-Auth-Token")

    val token = tokenId match {
      case tkn: Some[String] => tkn
      case _ =>
        request.session.get("X-Auth-Token") match {
          case tkn: Some[String] => tkn
          case _ => None
        }
    }

    val result = token match {
      case Some(tkn: String) => Token.withUser(tkn) { usr =>
        block(new AuthenticatedRequest[A](usr, request))
      }
      case _ => Future.successful(Forbidden("You must be signed in to view this page."))
    }

    result match {
      case None => Future.successful(Forbidden("You must be signed in to view this page."))
      case Some(res: Future[Result]) => res
    }
  }

  def isAuthenticated[A](request: Request[A]): Boolean = {
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