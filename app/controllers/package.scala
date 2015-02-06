import play.api.libs.json._
import play.api.libs.functional.syntax._

import business.domain._
import play.api.libs.oauth.RequestToken

package object controllers {
  //TODO: Make these correct with respect to direction and objects being allowed to be sent client side
  implicit val requestTokenJsonFormatter = Json.format[RequestToken]
  implicit val userJsonFormatter = Json.format[User]
  implicit val tokenJsonFormatter = Json.format[Token]
  implicit val registrationJsonFormatter = Json.format[Registration]
  implicit val preparedPotentialMatchJsonFormatter = Json.format[PreparedPotentialMatch]
  implicit val potentialMatchResponseJsonFormatter = Json.format[PotentialMatchResponse]
  implicit val preparedMatchJsonFormatter = Json.format[PreparedMatch]
}