package controllers

import business.logic.MatchingManager
import play.api.libs.json.Json
import play.api.mvc.Controller

object MobileController extends Controller {

  def getPotentialMatches = AuthAction { request =>
    val potentialMatches = MatchingManager.getPotentialMatches(request.user)
    Ok(Json.toJson(potentialMatches))
  }
}
