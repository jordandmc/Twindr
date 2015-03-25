package controllers

import business.logic.TestManager
import controllers.Application._
import play.api.mvc.Action

object TestController {

  /**
   * Drops the database and loads a predefined testing database
   * @return OK
   */
  def resetDatabase = Action { implicit request =>
    TestManager.resetDatabase
    Ok("")
  }

}
