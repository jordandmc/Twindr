package controllers

import business.domain.User
import business.logic.GeoJSONFormatter
import play.api.mvc.Controller

object MatchingController extends Controller {

  def updateGeolocation(latitude: Double, longitude: Double) = AuthAction { implicit request =>
    val location = GeoJSONFormatter.generateFromCoords(longitude, latitude)
    User.updateUserLocation(request.user, Option(location))
    Ok("Geolocation updated")
  }

  def acceptMatch(twitterName: String) = AuthAction { implicit request =>
    Ok("Success: " + twitterName)
  }

  def rejectMatch(twitterName: String) = AuthAction { implicit request =>
    Ok("")
  }

}
