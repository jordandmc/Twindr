package business.scaffolding

import business.domain.{Token, User}
import business.logic.GeoJSONFormatter
import play.api.libs.oauth.RequestToken

import scala.util.Random

trait WithUsers extends WithApplicationAndDatabase {
  val user1 = User("123456", RequestToken("X-Auth-Token", "123"), "testuser", Option("M"), Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())
  val user2 = User("123457", RequestToken("X-Auth-Token", "124"), "testuser2", None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())

  val token1 = Token("123", "123456")
  val token2 = Token("124", "123457")

  val user1Data = "X-Auth-Token" -> "123"
  val user2Data = "X-Auth-Token" -> "124"

  override def setup(): Unit = {
    super.setup()

    user1.save()
    user2.save()
    token1.save()
    token2.save()
  }
}
