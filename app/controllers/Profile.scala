package controllers

import business.domain.{UpdateRegistration, Registration}
import business.logic.RegistrationManager
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller

/**
 * Handles profile settings and filters
 */
object Profile extends Controller {

  val registrationForm = Form(
    mapping (
      "interests" -> text
    )(UpdateRegistration.apply)(UpdateRegistration.unapply)
  )

  def settings = AuthAction { implicit request =>
    val userData = UpdateRegistration(request.user.interests.mkString("\n"))
    Ok(views.html.settings(registrationForm.fill(userData))(request, request.user.twitterName))
  }

  def updateSettings = AuthAction { implicit request =>
    registrationForm.bindFromRequest(request.request.body.asFormUrlEncoded.getOrElse(Map())).fold(
      formWithErrors => {
        BadRequest(views.html.settings(formWithErrors)(request, request.user.twitterName))
      },
      registration => {
        RegistrationManager.register(request.user, Registration(request.user.sex.get, request.user.dateOfBirth.get, registration.interests))
        Redirect(routes.Profile.settings()).flashing("success" -> "Your account has been updated successfully.")
      }
    )
  }

}
