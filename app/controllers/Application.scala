package controllers

import business.domain.User
import business.logic.{MatchingManager, RegistrationManager}
import play.api.mvc._

object Application extends Controller {

  def index = Action { implicit request =>
    if(AuthAction.isAuthenticated(request)) {
      val user = AuthAction.getUserFromRequest(request)
      user match {
        case None => Ok(views.html.index(request))
        case Some(u: User) if RegistrationManager.hasRegistered(u) => Redirect(routes.Application.matchesFeed())
        case _ => Redirect(routes.Login.register())
      }
    }
    else {
      Ok(views.html.index(request))
    }
  }

  def matchesFeed = AuthAction { implicit request =>
    val user = User.updateUserTweets(request.user, TwitterProvider.timeline(request))
    val potentialMatches = MatchingManager.getPotentialMatches(user)

    Ok(views.html.matchesFeed(potentialMatches))
  }
}