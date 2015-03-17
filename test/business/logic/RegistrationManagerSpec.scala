package business.logic

import business.domain.{Registration, User}
import java.util.Date
import business.scaffolding.WithUsers
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._

@RunWith(classOf[JUnitRunner])
class RegistrationManagerSpec extends Specification {

  "RegistrationManager" should {

    "not already be registered" in new WithUsers {
      RegistrationManager.hasRegistered(user2) must be equalTo false
    }

    "already be registered" in new WithUsers {
      RegistrationManager.hasRegistered(user1) must be equalTo true
    }

    "not register with missing data" in new WithUsers {
      val info = Registration("", null, "")
      RegistrationManager.register(user2, info)

      val user = User.getByID(user2._id).get
      RegistrationManager.hasRegistered(user2) must be equalTo false
    }

    "register with valid data" in new WithUsers {
      val info = Registration("M", new Date(), "apples")
      RegistrationManager.register(user2, info)

      val user = User.getByID(user2._id).get
      RegistrationManager.hasRegistered(user) must be equalTo true
    }

  }

}
