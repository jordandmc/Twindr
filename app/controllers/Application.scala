package controllers

import java.util.UUID

import business.User
import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("test")
  }

}