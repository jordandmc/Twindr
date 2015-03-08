package controllers

import business.domain.{PreparedPotentialMatch, PotentialMatch, PotentialMatchResponse, User}
import business.logic.{MatchingManager, GeoJSONFormatter}
import play.api.libs.json.{JsValue, Writes, Json}
import play.api.mvc.Controller
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MatchingController extends Controller {

  implicit val potentialMatchWrites = new Writes[PreparedPotentialMatch] {
    def writes(p: PreparedPotentialMatch): JsValue = {
      Json.obj(
        "username" -> p.username,
        "tweets" -> p.tweets.take(3)
      )
    }
  }

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

    Ok("Accepted")
  }

  def rejectMatch(twitterName: String) = AuthAction { implicit request =>
    //Process the match response to this user asynchronously
    Future {
      MatchingManager.processMatchResponse(request.user, PotentialMatchResponse(twitterName, PotentialMatch.REJECTED))
    }

    Ok("Rejected")
  }

  def unmatch(twitterName: String) = AuthAction { implicit request =>
    Future {
      MatchingManager.unmatch(request.user, twitterName)
    }

    Ok("Unmatched")
  }

  def getPotentialMatches = AuthAction { implicit request =>
    val potentialMatches = MatchingManager.getPotentialMatches(request.user)
    if(potentialMatches.nonEmpty)
    {
      Ok(Json.obj(
        "matches" -> potentialMatches
      ))
    }
    else
      BadRequest("No matches")
  }

  def matchedUserFeed = AuthAction { implicit request =>
    Ok(views.html.matchedUserFeed(MatchingManager.getMatches(request.user)))
  }
}
