package controllers

import business.domain.Registration
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

object Login extends Controller {

  val registrationForm = Form (
    mapping (
      "sex" -> text.verifying("Please enter your sex", {!_.isEmpty}),
      "birthday" -> date.verifying("Your birthday must be in the past", {_.before(new java.util.Date())}),
      "preferredLocation" -> text.verifying("Please enter your preferred location", {!_.isEmpty}),
      "interests" -> text.verifying("Please enter at least one interest of yours", {!_.isEmpty})
    ) (Registration.apply)(Registration.unapply)
  )

  def login = Action {
    Ok("login")
  }

  def logout = AuthAction {
    Redirect(routes.Application.index()).withNewSession
  }

  def register = AuthAction {
    Ok(views.html.register(registrationForm))
  }

  def checkRegistration = AuthAction { implicit request =>
    registrationForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      registration => {
        // Process registration information
        // Add to database

        Redirect(routes.Application.matchesFeed())
      }
    )
  }

}
