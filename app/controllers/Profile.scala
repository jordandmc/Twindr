package controllers

import play.api.mvc.Controller

/**
 * Handles profile settings and filters
 */
object Profile extends Controller {

  def settings = AuthAction { implicit request =>
    Ok(views.html.settings(request))
  }

}
