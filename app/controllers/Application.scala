package controllers

import java.util.UUID
import business.domain.User
import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("test")
  }

}