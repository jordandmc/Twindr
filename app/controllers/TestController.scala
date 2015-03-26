package controllers

import business.logic.TestManager
import controllers.Application._
import play.Play
import play.api.mvc.Action

object TestController {

  /**
   * Drops the database and loads a predefined testing database
   * @return OK
   */
  def resetDatabase = Action { implicit request =>
    if(Play.application.configuration.getString("allowDBReset") == "true") {
      TestManager.resetDatabase
      Ok("Database reset!")
    } else {
      Ok("Failed to reset database.")
    }
  }

}
