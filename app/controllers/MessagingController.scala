package controllers

import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Concurrent}
import play.api.libs.json.{Json, JsValue}
import play.api.mvc._

object MessagingController extends Controller {

  /**
   * Enumerator for pushing JSON data through the sent event
   */
  val (msg, msgOutChannel) = Concurrent.broadcast[JsValue]

  /**
   * Show the messaging page
   * @param recipientTwitterName Twitter name of the user we want to send a message to
   * @return The messaging page response
   */
  def messages(recipientTwitterName: String) = AuthAction { implicit request =>
    Ok(views.html.messaging(request.user.twitterName, recipientTwitterName, "1")(request))
  }

  /**
   * Sends the given message from the client to the server
   * @return Success
   */
  def sendMessage = AuthAction(parse.json) { implicit request =>
    msgOutChannel.push(request.body)
    Ok
  }

  /**
   * Receives a server sent event and informs the client
   * @return EventSource object containing the message
   */
  def receiveMessage(matchID: String) = AuthAction { implicit request =>
    Ok.feed(msg &> filter(matchID) &>  Concurrent.buffer(50) &> EventSource()).as("text/event-stream")
  }

  /**
   * Filters messages based on a particular match
   * @param matchID The ID associated with a particular matching
   * @return Filtered messages on matchID
   */
  private def filter(matchID: String) = Enumeratee.filter[JsValue] { json: JsValue => (json \ "matchID").as[String] == matchID}

}
