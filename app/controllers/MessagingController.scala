package controllers

import play.api.libs.EventSource
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsValue
import play.api.mvc._

object MessagingController extends Controller {

  /**
   * Enumerator for pushing JSON data through the sent event
   */
  val (out, wsOutChannel) = Concurrent.broadcast[JsValue]

  /**
   * Show the messaging page
   * @return The messaging page response
   */
  def messages = AuthAction { implicit request =>
    Ok(views.html.messaging(request))
  }

  /**
   * Sends the given message from the client to the server
   * @return Success
   */
  def sendMessage = AuthAction(parse.json) { implicit request =>
    wsOutChannel.push(request.body)
    Ok
  }

  /**
   * Receives a server sent event and informs the client
   * @return EventSource object containing the message
   */
  def receiveMessage = AuthAction { implicit request =>
    Ok.feed(out &> EventSource()).as("text/event-stream")
  }

}
