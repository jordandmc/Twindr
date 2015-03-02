package controllers

import business.domain.{PotentialMatch, PotentialMatchResponse, User}
import business.logic.{MatchingManager, GeoJSONFormatter}
import play.api.libs.json.{JsObject, Json}
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

    //Assume that the first match in the list is the match that we are in the process of
    //marking as accepted
    val nextMatch = getNextMatch(request.user)
    if(nextMatch.nonEmpty)
      Ok(nextMatch.get)
    else
      BadRequest("No matches")
  }

  def rejectMatch(twitterName: String) = AuthAction { implicit request =>
    //Process the match response to this user asynchronously
    Future {
      MatchingManager.processMatchResponse(request.user, PotentialMatchResponse(twitterName, PotentialMatch.REJECTED))
    }

    //Assume that the first match in the list is the match that we are in the process of
    //marking as accepted
    val nextMatch = getNextMatch(request.user)
    if(nextMatch.nonEmpty)
      Ok(nextMatch.get)
    else
      BadRequest("No matches")
  }

  def getFirstMatch = AuthAction { implicit request =>
    val potentialMatches = MatchingManager.getPotentialMatches(request.user)
    if(potentialMatches.nonEmpty)
    {
      Ok(Json.obj(
        "username" -> potentialMatches(0).username,
        "tweets" -> potentialMatches(0).tweets.take(3)
      ))
    }
    else
      BadRequest("No matches")
  }

  private def getNextMatch(user: User): Option[JsObject] = {
    val potentialMatches = MatchingManager.getPotentialMatches(user)
    if(potentialMatches.size > 1)
    {
      Option(Json.obj(
        "username" -> potentialMatches(1).username,
        "tweets" -> potentialMatches(1).tweets.take(3)
      ))
    }
    else
      None
  }

}
