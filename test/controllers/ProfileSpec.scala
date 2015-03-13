package controllers

import java.util.Date

import business.domain.{Registration, User}
import business.logic.{RegistrationManager, WithApplicationAndDatabase}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ProfileSpec extends Specification {

  "Profile#settings" should {

    "access page if signed in" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.Profile.settings().url).withHeaders(user1Data).withSession(user1Data)
      val resp = route(request).get

      AuthAction.isAuthenticated(request) must beEqualTo(true)
      status(resp) must equalTo(OK)
    }

    "not access page if not signed in" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.Profile.settings().url)
      AuthAction.isAuthenticated(request) must beEqualTo(false)
      redirectLocation(route(request).get) must beSome.which(_ == controllers.routes.Application.index().url)
    }

    "not update with missing data" in new WithApplicationAndDatabase {
      val info = Registration("", null, "")
      RegistrationManager.register(user1, info)
      RegistrationManager.hasRegistered(user1) must be equalTo true

      user1.sex must not be equalTo("")
      user1.dateOfBirth must not be equalTo(null)
    }

    "update with valid data" in new WithApplicationAndDatabase {
      val info = Registration("M", new Date(), "apples")
      RegistrationManager.register(user1, info)
      RegistrationManager.hasRegistered(user1) must be equalTo true
    }

  }
}
