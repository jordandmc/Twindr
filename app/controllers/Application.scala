package controllers

import business.domain.{User, Token}
import business.logic.RegistrationManager
import controllers.Login._
import play.api.mvc._

object Application extends Controller {

  def index = Action { request =>
    if(AuthAction.isAuthenticated(request)) {
      val user = AuthAction.getUserFromRequest(request)
      user match {
        case None => Ok(views.html.index())
        case Some(u: User) if RegistrationManager.hasRegistered(u) => Redirect(routes.Application.matchesFeed())
        case _ => Redirect(routes.Login.register())
      }
    }
    else {
      Ok(views.html.index())
    }
  }

  def matchesFeed = AuthAction { request =>
    Ok(views.html.matchesFeed())
  }
}