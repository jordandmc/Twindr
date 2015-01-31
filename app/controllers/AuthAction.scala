package controllers

import play.api.mvc.Results._
import play.api.mvc.{ActionBuilder, Request, Result}
import scala.concurrent.Future

object AuthAction extends ActionBuilder[Request] {

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    if(isAuthenticated(request)) {
      block(request)
    }
    else {
      Future.successful(Forbidden("You must be signed in to view this page."))
    }
  }

  def isAuthenticated[A](request: Request[A]) : Boolean = {
    //Replace this: Check for a user id/ username in the session data
    return !request.session.isEmpty
  }
}