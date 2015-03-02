package controllers

import business.domain.{PotentialMatch, PotentialMatchResponse, User}
import business.logic.{MatchingManager, GeoJSONFormatter}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Controller
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MatchingController extends Controller {

  def updateGeolocation(latitude: Double, longitude: Double) = AuthAction { implicit request =>
    val location = GeoJSONFormatter.generateFromCoords(longitude, latitude)
    User.updateUserLocation(request.user, Option(location))
    Ok("Geolocation updated")
  }

  def acceptMatch(twitterName: String) = AuthAction { implicit request =>
    //Process the match response to this user asynchronously
    Future {
      MatchingManager.processMatchResponse(request.user, PotentialMatchResponse(twitterName, PotentialMatch.ACCEPTED))
    }

    //Return a new match
    val potentialMatches = MatchingManager.getPotentialMatches(request.user)
    if(!potentialMatches.isEmpty)
    {
      Ok(Json.obj(
        "username" -> potentialMatches(0).username,
        "tweets" -> potentialMatches(0).tweets.take(3)
      ))
    }
    else
      BadRequest("No matches")
  }

  def rejectMatch(twitterName: String) = AuthAction { implicit request =>
    Ok("")
  }

  def getFirstMatch = AuthAction { implicit request =>
    val potentialMatches = MatchingManager.getPotentialMatches(request.user)
    if(!potentialMatches.isEmpty)
    {
      Ok(Json.obj(
        "username" -> potentialMatches(0).username,
        "tweets" -> potentialMatches(0).tweets.take(3)
      ))
    }
    else
      BadRequest("No matches")
  }

}
