package controllers

import business.logic.WithApplicationAndDatabase
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class MatchingControllerSpec extends Specification {

  "updateGeolocation" should {

    "update the location" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MatchingController.updateGeolocation(0, 0).url).withSession(user1Data).withHeaders(user1Data)
      val req = route(request).get

      status(req) must equalTo(OK)
    }

    "update the location 2" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MatchingController.updateGeolocation(48.2931, -98.21112).url).withSession(user1Data).withHeaders(user1Data)
      val req = route(request).get

      status(req) must equalTo(OK)
    }

    "update the location 3" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MatchingController.updateGeolocation(-11.22111, 30.0121).url).withSession(user1Data).withHeaders(user1Data)
      val req = route(request).get

      status(req) must equalTo(OK)
    }

    "update the location 4" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MatchingController.updateGeolocation(10, 20).url).withSession(user1Data).withHeaders(user1Data)
      val req = route(request).get

      status(req) must equalTo(OK)
    }

    "update the location 5" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MatchingController.updateGeolocation(-12.32211, -123.11221).url).withSession(user1Data).withHeaders(user1Data)
      val req = route(request).get

      status(req) must equalTo(OK)
    }

  }

  "matchedUserFeed" should {

    "not render the matched user page (if not signed in)" in new WithApplication {
      val req = route(FakeRequest(GET, controllers.routes.MatchingController.matchedUserFeed().url)).get

      status(req) must equalTo(SEE_OTHER)
      redirectLocation(req) must beSome.which(_ == controllers.routes.Application.index().url)
    }

    "render the matched user page" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MatchingController.matchedUserFeed().url).withSession(user1Data).withHeaders(user1Data)
      val req = route(request).get

      status(req) must equalTo(OK)
      contentAsString(req) must contain ("Your matches:")
      assert(AuthAction.isAuthenticated(request))
    }

  }

}
