package business.logic

import business.domain.{Registration, User}
import java.util.Date
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._
import play.api.libs.oauth.RequestToken

@RunWith(classOf[JUnitRunner])
class RegistrationManagerSpec extends Specification {

  "RegistrationManager" should {

    "not already be registered" in new WithApplicationAndDatabase {
      val user = User("123457", RequestToken("124","123457"), "twitterName", None, None, None, List(), List(), 1)
      RegistrationManager.hasRegistered(user) must be equalTo false
    }

    "already be registered" in new WithApplicationAndDatabase {
      val user = User("123456", RequestToken("123","123456"), "twitterName", Option("M"), Option(new Date()), None, List(), List(), 1)
      RegistrationManager.hasRegistered(user) must be equalTo true
    }

    "not register with missing data" in new WithApplicationAndDatabase {
      val user = User("123457", RequestToken("124","123457"), "twitterName", None, None, None, List(), List(), 1)
      val info = Registration("", null, "")
      RegistrationManager.register(user, info)
      RegistrationManager.hasRegistered(user) must be equalTo false
    }

    "register with valid data" in new WithApplicationAndDatabase {
      val user = User("124", RequestToken("124","123457"), "twitterName", Option("M"), Option(new Date()), None, List(), List(), 1)
      val info = Registration("M", new Date(), "apples")
      RegistrationManager.register(user, info)
      RegistrationManager.hasRegistered(user) must be equalTo true
    }

  }

}
