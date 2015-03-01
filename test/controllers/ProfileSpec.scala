package controllers

import java.util.Date

import business.domain.{Registration, User}
import business.logic.{RegistrationManager, WithApplicationAndDatabase}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.oauth.RequestToken
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
      val user = User("123457", RequestToken("124","123457"), "twitterName", None, None, None, List(), List(), 1)
      val info = Registration("", null, "")
      RegistrationManager.register(user, info)
      RegistrationManager.hasRegistered(user) must be equalTo false
    }

    "update with valid data" in new WithApplicationAndDatabase {
      val user = User("124", RequestToken("124","123457"), "twitterName", Option("M"), Option(new Date()), None, List(), List(), 1)
      val info = Registration("M", new Date(), "apples")
      RegistrationManager.register(user, info)
      RegistrationManager.hasRegistered(user) must be equalTo true
    }

  }

  "Profile#filters" should {

    "access page if signed in" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.Profile.filters().url).withHeaders(user1Data).withSession(user1Data)
      AuthAction.isAuthenticated(request) must beEqualTo(true)
      status(route(request).get) must equalTo(OK)
    }

    "not access page if not signed in" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.Profile.filters().url)
      AuthAction.isAuthenticated(request) must beEqualTo(false)
      redirectLocation(route(request).get) must beSome.which(_ == controllers.routes.Application.index().url)
    }

  }
}
