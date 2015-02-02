package controllers

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

  }
  
  "Application#index" should {

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Welcome to Twindr!")
    }
    
    "not render the index page if signed in" in new WithApplication {
      val request = FakeRequest(GET, "/").withSession("userid" -> "1234")
      val home = route(request).get

      status(home) must equalTo(SEE_OTHER) // Should redirect automatically
      redirectLocation(home) must beSome.which(_ == "/main")
      contentAsString(home) must not contain ("Welcome to Twindr!")
      assert(AuthAction.isAuthenticated(request))
    }
    
  }

  "Application#main" should {

    "render the matching feed page (if signed in)" in new WithApplication {
      val request = FakeRequest(GET, "/main").withSession("userid" -> "1234")
      val main = route(request).get

      status(main) must equalTo(OK)
      contentType(main) must beSome.which(_ == "text/html")
      contentAsString(main) must contain ("View your matches here")
      assert(AuthAction.isAuthenticated(request))
    }

    "not render the matching feed page if not signed in" in new WithApplication {
      val request = FakeRequest(GET, "/main")
      val main = route(request).get

      status(main) must equalTo(FORBIDDEN)
      contentType(main) must beSome.which(_ == "text/plain")
      contentAsString(main) must contain ("You must be signed in to view this page.")
      assert(!AuthAction.isAuthenticated(request))
    }

  }
}
