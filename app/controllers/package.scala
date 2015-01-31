import play.api.libs.json._
import play.api.libs.functional.syntax._

import business.domain._
import play.api.libs.oauth.RequestToken

package object controllers {
  implicit val requestTokenJsonFormatter = Json.format[RequestToken]
  implicit val userJsonFormatter = Json.format[User]
  implicit val tokenJsonFormatter = Json.format[Token]
  implicit val registrationJsonFormatter = Json.format[Registration]
}
