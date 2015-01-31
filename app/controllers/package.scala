import play.api.libs.json._
import play.api.libs.functional.syntax._

import business.domain._

package object controllers {
  implicit val userJsonFormatter = Json.format[User]
  implicit val tokenJsonFormatter = Json.format[Token]
  implicit val registrationJsonFormatter = Json.format[Registration]
}
