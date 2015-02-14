package controllers

import business.domain.Registration
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
    Ok(views.html.settings(registrationForm)(request))
  }

  def filters = AuthAction { implicit request =>
    Ok(views.html.filters(request))
  }

}
