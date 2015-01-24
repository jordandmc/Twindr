package controllers

import java.util.UUID

import business.User
import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    val id = UUID.randomUUID().toString
    val originalUser = User(id, "testToken")
    originalUser.save()

    val newUser = User.getByID(id)
    val newId = newUser.get.id

    val result = if(id == newId) {
      "Correct"
    } else {
      "Incorrect"
    }

    Ok(result)
  }

}