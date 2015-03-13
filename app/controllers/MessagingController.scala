package controllers

import play.api.mvc._

object MessagingController extends Controller {

  def messages = AuthAction { implicit request =>
    Ok(views.html.messaging(request))
  }

}
