package business.logic

import business.domain.{Token, User}
import com.typesafe.config.ConfigFactory
import org.specs2.execute.{AsResult, Result}
import persistence.DBManager._
import play.api.{Configuration, GlobalSettings}
import play.api.libs.oauth.RequestToken
import play.api.test.{FakeApplication, WithApplication}
import scala.util.Random

/**
 * Allows tests to run with a fake application and the testing database.
 */
trait WithApplicationAndDatabase extends WithApplication {

  /**
   * Changes the application to use the test configuration file
   * @return A FakeApplication running the test settings
   */
  override implicit val app: FakeApplication = new FakeApplication(
    withGlobal = Some(new GlobalSettings() {
      override def configuration: Configuration = {
        Configuration(ConfigFactory.load("test.conf"))
      }
    })
  )

  /**
   * Generic user test data. These users will be populated in the database once created
   * for use with tests. Ideally use these to speed up the process with unrelated tests.
   * ie. Make new ones if you're testing user creation
   */
  val user1 = User("123456", RequestToken("X-Auth-Token", "123"), "testuser", Option("M"), Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())
  val user2 = User("123457", RequestToken("X-Auth-Token", "124"), "testuser2", None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())

  val token1 = Token("123", "123456")
  val token2 = Token("124", "123457")

  val user1Data = ("X-Auth-Token" -> "123")
  val user2Data = ("X-Auth-Token" -> "124")

  override def around[T: AsResult](t: => T): Result = super.around {
    setup
    try {
      t
    } finally {
      teardown
    }
  }

  /**
   * Populate the test database with test data
   */
  def setup: Unit = {
    user1.save()
    user2.save()
    token1.save()
    token2.save()
  }

  /**
   * Clear the test database
   */
  def teardown: Unit = withDB { db =>
    db.getCollection(User.collection).drop
    db.getCollection(Token.collection).drop
  }
}