package controllers

import business.domain.{Token, Registration}
import business.logic.RegistrationManager
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

object Login extends Controller {

  val registrationForm = Form(
    mapping (
      "sex" -> text.verifying("Please enter your sex", {!_.isEmpty}),
      "birthday" -> date.verifying("Your birth date must be in the past", {_.before(new java.util.Date())}),
      "interests" -> text
    )(Registration.apply)(Registration.unapply)
  )

  def login = AuthAction { request =>
    if(RegistrationManager.hasRegistered(request.user)) {
      Redirect(routes.Application.matchesFeed())
    }
    else {
      Redirect(routes.Login.register())
    }
  }

  def logout = AuthAction { request =>
    AuthAction.getTokenString(request).map{ tkn =>
      Token.deleteById(tkn)
    }

    Redirect(routes.Application.index()).withNewSession
  }

  def register = AuthAction { request =>
    if(!RegistrationManager.hasRegistered(request.user)) {
      Ok(views.html.register(registrationForm))
    }
    else {
      Forbidden("Your account has already registered")
    }
  }

  def checkRegistration = AuthAction { request =>
    registrationForm.bindFromRequest((request.request.body.asFormUrlEncoded).getOrElse(Map())).fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      registration => {
        RegistrationManager.register(request.user, registration)
        Redirect(routes.Application.matchesFeed())
      }
    )
  }

}
