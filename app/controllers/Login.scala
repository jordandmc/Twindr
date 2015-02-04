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
      "interests" -> text.verifying("Please enter at least one interest of yours", {!_.isEmpty})
    )(Registration.apply)(Registration.unapply)
  )

  def login = Action {
    Ok("login")
  }

  def logout = AuthAction { request =>
    AuthAction.getTokenString(request).map{ tkn =>
      Token.deleteById(tkn)
    }

    Redirect(routes.Application.index()).withNewSession
  }

  def register = AuthAction { request =>
    Ok(views.html.register(registrationForm))
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
