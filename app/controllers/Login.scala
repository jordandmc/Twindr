package controllers

import play.api.mvc.{Action, Controller}

object Login extends Controller {

  def signInWithTwitter = Action {
    Ok("Redirect to Twitter")
  }

  def login = Action {
    Ok("Received Twitter token")
  }

  def logout = AuthAction {
    Redirect(routes.Application.index()).withNewSession
  }

  def register = AuthAction {
    Ok(views.html.register())
  }

  def checkRegistration = AuthAction {
    Ok("Validate registration info and redirect to main macthmaking feed page")
  }

}
