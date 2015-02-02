package controllers

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

      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/plain")
      contentAsString(login) must contain ("login")
    }

  }

  "Login#logout" should {

    "logout of the application (logged in)" in new WithApplication {
      val logout = route(FakeRequest(GET, controllers.routes.Login.logout().url).withSession("userid" -> "1234")).get

      status(logout) must equalTo(SEE_OTHER)
      redirectLocation(logout) must beSome.which(_ == controllers.routes.Application.index().url)
    }

    "logout of the application (not logged in)" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.Login.logout().url)
      val logout = route(request).get

      status(logout) must equalTo(FORBIDDEN)
      contentType(logout) must beSome.which(_ == "text/plain")
      contentAsString(logout) must contain ("You must be signed in to view this page.")
      assert(!AuthAction.isAuthenticated(request))
    }

  }

  "Login#register" should {

    "view the registration page (logged in)" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.Login.register().url).withSession("userid" -> "1234")
      val register = route(request).get

      status(register) must equalTo(OK)
      contentType(register) must beSome.which(_ == "text/html")
      contentAsString(register) must contain ("Before you get started")
    }

    "view the registration page (not logged in)" in new WithApplication {
      val register = route(FakeRequest(GET, controllers.routes.Login.register().url)).get

      status(register) must equalTo(FORBIDDEN)
      contentType(register) must beSome.which(_ == "text/plain")
      contentAsString(register) must contain ("You must be signed in to view this page.")
    }

    "submit registration data (empty)" in new WithApplication {
      val register = route(FakeRequest(POST, controllers.routes.Login.register().url).withSession("userid" -> "1234")).get

      status(register) must equalTo(BAD_REQUEST)
      contentType(register) must beSome.which(_ == "text/html")
      contentAsString(register) must contain ("The following errors occurred")
    }

  }
}