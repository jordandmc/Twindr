package controllers

import business.domain.PotentialMatchResponse
import business.logic.MatchingManager
import play.api.libs.json.{JsValue, JsSuccess, Json}
import play.api.mvc.Controller

object MobileController extends Controller {

  def getPotentialMatches = AuthAction { request =>
    val potentialMatches = MatchingManager.getPotentialMatches(request.user)
    Ok(Json.toJson(potentialMatches))
  }

  def processMatchResponse = AuthAction { request =>
    request.body.asJson match {
      case Some(js: JsValue) =>
        Json.fromJson[PotentialMatchResponse](js) match {
          case response: JsSuccess[PotentialMatchResponse] =>
            MatchingManager.processMatchResponse(request.user, response.get)
            Ok
          case _ =>
            BadRequest("Invalid JSON")
        }
      case _ =>
        BadRequest("Invalid Response")
    }
  }

  def getMatches = AuthAction { request =>
    val matches = MatchingManager.getMatches(request.user)
    Ok(Json.toJson(matches))
  }

  def unmatch = AuthAction { request =>
    MatchingManager.unmatch(request.user,request.body.asText.getOrElse(""))
    Ok
  }
}
