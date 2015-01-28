package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def matchesFeed = AuthAction {
    Ok(views.html.matchesFeed())
  }

}