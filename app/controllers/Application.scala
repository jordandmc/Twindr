package controllers

import business.logic.LoginManager
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.ws.WS
import play.api.mvc._

import scala.concurrent.Future

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def matchesFeed = AuthAction {
    Ok(views.html.matchesFeed())
  }
}