package controllers

import business.domain.MatchMessage
import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Concurrent}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MessagingController extends Controller {

  implicit val matchMessageReads = Json.reads[MatchMessage]
  implicit val matchMessageWrite = Json.writes[MatchMessage]

  /**
   * Enumerator for pushing JSON data through the sent event
   */
  val (msg, msgOutChannel) = Concurrent.broadcast[JsValue]

  /**
   * Show the messaging page
   * @param matchID Unique identifier used to identify both the match object and the communication channel
   * @param recipient The name of the user we are communicating with
   * @return The messaging page response
   */
  def messages(matchID: String, recipient: String) = AuthAction { implicit request =>
    Ok(views.html.messaging(request.user.twitterName, recipient, matchID)(request, request.user.twitterName))
  }

  /**
   * Sends the given message from the client to the server and saves the message
   * @return Success
   */
  def sendMessage = AuthAction { implicit request =>
    request.body.asJson match {
      case Some(js: JsValue) =>
        Json.fromJson[MatchMessage](js) match {
          case response: JsSuccess[MatchMessage] =>
            Future {
              //Make a record of the message in the database
              MatchMessage.processNewMessage(response.get)
            }
            msgOutChannel.push(js)
            Ok
          case _ =>
            BadRequest("Invalid JSON")
        }
      case _ =>
        BadRequest("Invalid Response")
    }
  }

  /**
   * Receives a server sent event and informs the client
   * @return EventSource object containing the message
   */
  def receiveMessage(matchID: String) = AuthAction { implicit request =>
    Ok.feed(msg &> filter(matchID) &>  Concurrent.buffer(50) &> EventSource()).as("text/event-stream")
  }

  /**
   * Loads previous messages from the data
   * @param matchID The match to load messages for
   * @return The messages
   */
  def getMoreMessages(matchID: String) = AuthAction { implicit request =>
    val previousMessages = MatchMessage.retrievePreviousMessage(matchID, new java.util.Date())
    Ok(Json.toJson(previousMessages))
  }

  /**
   * Filters messages based on a particular match
   * @param matchID The ID associated with a particular matching
   * @return Filtered messages on matchID
   */
  private def filter(matchID: String) = Enumeratee.filter[JsValue] { json: JsValue => (json \ "matchID").as[String] == matchID}

}
