package controllers

import business.domain.{Token, Registration}
import business.logic.{LoginManager, RegistrationManager}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller

object Login extends Controller {

  val registrationForm = Form(
    mapping (
      "sex" -> text.verifying("Please enter your sex", {!_.isEmpty}),
      "birthday" -> date("yyyy-mm-dd").verifying("Your birth date must be in the past", {_.before(new java.util.Date())}),
      "interests" -> text
    )(Registration.apply)(Registration.unapply)
  )

  def login = AuthAction { implicit request =>
    if(RegistrationManager.hasRegistered(request.user)) {
      Redirect(routes.Application.matchesFeed())
    }
    else {
      Redirect(routes.Login.register())
    }
  }

  def logout = AuthAction { implicit request =>
    LoginManager.logout(request.token)

    Redirect(routes.Application.index()).withNewSession
  }

  def register = AuthAction { implicit request =>
    if(!RegistrationManager.hasRegistered(request.user)) {
      Ok(views.html.register(registrationForm))
    }
    else {
      Redirect(routes.Application.matchesFeed())
    }
  }

  def checkRegistration = AuthAction { implicit request =>
    registrationForm.bindFromRequest(request.request.body.asFormUrlEncoded.getOrElse(Map())).fold(
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
