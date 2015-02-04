package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action { request =>
    if(AuthAction.isAuthenticated(request)) {
      Redirect(routes.Application.matchesFeed())
    }
    else {
      Ok(views.html.index())
    }
  }

  def matchesFeed = AuthAction { request =>
    Ok(views.html.matchesFeed())
  }
}