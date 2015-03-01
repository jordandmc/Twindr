package controllers

import play.api.mvc.{Action, Controller}

object MatchingController extends Controller {

  def updateGeolocation(latitude: Double, longitude: Double) = AuthAction { implicit request =>
    Ok("Success: Lat=" + latitude + " Long: " + longitude)
  }

  def acceptMatch(twitterName: String) = AuthAction { implicit request =>
    Ok("Success: " + twitterName)
  }

  def rejectMatch(twitterName: String) = AuthAction { implicit request =>
    Ok("")
  }

}
