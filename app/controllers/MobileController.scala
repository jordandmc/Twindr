package controllers

import business.domain.{UpdateRegistration, Registration, PotentialMatchResponse}
import business.logic.{RegistrationManager, LoginManager, MatchingManager}
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

  def logout = AuthAction { request =>
    LoginManager.logout(request.token)
    Ok
  }

  def registerUser = AuthAction { request =>
    request.body.asJson match {
      case Some(js: JsValue) =>
        Json.fromJson[Registration](js) match {
          case response: JsSuccess[Registration] =>
            RegistrationManager.register(request.user, response.get)
            Ok
          case _ =>
            Json.fromJson[UpdateRegistration](js) match {
              case response: JsSuccess[UpdateRegistration] =>
                response.foreach { rsp =>
                  RegistrationManager.updateProfile(request.user, rsp.interests)
                }

                Ok
              case _ =>
                BadRequest ("Invalid JSON")
            }
        }
      case _ =>
        BadRequest("Invalid Response")
    }
  }

  def getProfileInformation = AuthAction { request =>
    Ok(Json.toJson(UpdateRegistration(request.user.interests.mkString("\n"))))
  }
}
