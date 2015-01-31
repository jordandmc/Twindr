package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action { implicit request =>
    if(AuthAction.isAuthenticated(request)) {
      Redirect(routes.Application.matchesFeed())
    }
    else {
      Ok(views.html.index())
    }
  }

  def matchesFeed = AuthAction {
    Ok(views.html.matchesFeed())
  }
}