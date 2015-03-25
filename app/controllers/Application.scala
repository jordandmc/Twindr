package controllers

import business.domain.User
import business.logic.RegistrationManager
import play.api.Routes
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
    //Update the user's tweets when they refresh the matches page
    User.updateUserTweets(request.user, TwitterProvider.timeline(request))
    Ok(views.html.matchesFeed())
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.MatchingController.acceptMatch,
        routes.javascript.MessagingController.getMoreMessages,
        routes.javascript.MatchingController.getPotentialMatches,
        routes.javascript.MessagingController.receiveMessage,
        routes.javascript.MatchingController.rejectMatch,
        routes.javascript.TestController.resetDatabase,
        routes.javascript.MessagingController.sendMessage,
        routes.javascript.MatchingController.unmatch,
        routes.javascript.MatchingController.updateGeolocation
      )
    ).as("text/javascript")
  }
}