package controllers

import business.scaffolding.WithUsers
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class LoginSpec extends Specification {

  "Login#login" should {

    "log in to the application" in new WithApplication {
      val login = route(FakeRequest(GET, controllers.routes.Login.login().url)).get

      status(login) must equalTo(SEE_OTHER)
      redirectLocation(login) must beSome.which(_ == controllers.routes.Application.index().url)
    }

  }

  "Login#logout" should {

    "logout of the application (logged in)" in new WithUsers {
      val logout = route(FakeRequest(GET, controllers.routes.Login.logout().url).withSession(user1Data).withHeaders(user1Data)).get

      status(logout) must equalTo(SEE_OTHER)
      redirectLocation(logout) must beSome.which(_ == controllers.routes.Application.index().url)
    }

    "logout of the application (not logged in)" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.Login.logout().url)
      val logout = route(request).get

      status(logout) must equalTo(SEE_OTHER)
      redirectLocation(logout) must beSome.which(_ == controllers.routes.Application.index().url)
      assert(!AuthAction.isAuthenticated(request))
    }

  }

  "Login#register" should {

    "view the registration page (logged in)" in new WithUsers {
      val request = FakeRequest(GET, controllers.routes.Login.register().url).withSession(user2Data).withHeaders(user2Data)
      val register = route(request).get

      status(register) must equalTo(OK)
      contentType(register) must beSome.which(_ == "text/html")
      contentAsString(register) must contain ("Before you get started")
    }

    "view the registration page (not logged in)" in new WithApplication {
      val register = route(FakeRequest(GET, controllers.routes.Login.register().url)).get

      status(register) must equalTo(SEE_OTHER)
      redirectLocation(register) must beSome.which(_ == controllers.routes.Application.index().url)
    }

    "submit registration data (empty)" in new WithUsers {
      val register = route(FakeRequest(POST, controllers.routes.Login.register().url).withSession(user2Data).withHeaders(user2Data)).get

      status(register) must equalTo(BAD_REQUEST)
      contentType(register) must beSome.which(_ == "text/html")
      contentAsString(register) must contain ("The following errors occurred")
    }

  }
}