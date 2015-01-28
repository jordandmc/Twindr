package controllers

import play.api.mvc.Results._
import play.api.mvc.{ActionBuilder, Request, Result}
import scala.concurrent.Future

object AuthAction extends ActionBuilder[Request] {

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    if(!request.session.isEmpty) {
      //Replace this: Check for a user id/ username in the session data
      block(request)
    }
    else {
      Future.successful(Forbidden("You must be signed in to view this page."))
    }
  }
}