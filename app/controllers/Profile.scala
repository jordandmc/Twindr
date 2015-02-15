package controllers

import business.domain.Registration
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
      "sex" -> text.verifying("Please enter your sex", {!_.isEmpty}),
      "birthday" -> date("yyyy-mm-dd").verifying("Your birth date must be in the past", {_.before(new java.util.Date())}),
      "interests" -> text
    )(Registration.apply)(Registration.unapply)
  )

  def settings = AuthAction { implicit request =>
    val userData = Registration(request.user.sex.toString, request.user.dateOfBirth.get, request.user.interests.mkString("\n"))
    Ok(views.html.settings(registrationForm.fill(userData))(request))
  }

  def updateSettings = AuthAction { implicit request =>
    registrationForm.bindFromRequest(request.request.body.asFormUrlEncoded.getOrElse(Map())).fold(
      formWithErrors => {
        BadRequest(views.html.settings(formWithErrors))
      },
      registration => {
        RegistrationManager.register(request.user, registration)
        Redirect(routes.Profile.settings()).flashing("success" -> "Your account has been updated successfully.")
      }
    )
  }

  def filters = AuthAction { implicit request =>
    Ok(views.html.filters(request))
  }

}
