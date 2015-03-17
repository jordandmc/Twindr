package controllers

import business.logic.WithApplicationAndDatabase
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class MessagingControllerSpec extends Specification {

  "messages" should {

    "not render the messages page if not signed in" in new WithApplication {
      val page = route(FakeRequest(GET, controllers.routes.MessagingController.messages("1", "twitterUser").url)).get

      status(page) must equalTo(SEE_OTHER)
      contentAsString(page) must not contain ("Messaging")
    }

    "render the messages page if signed in" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MessagingController.messages("1", "twitterUser").url).withSession(user1Data).withHeaders(user1Data)
      val page = route(request).get

      status(page) must equalTo(OK)
      assert(AuthAction.isAuthenticated(request))
    }

  }

  "sendMessage" should {

    "not process an unauthenticated request" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.MessagingController.sendMessage().url)
      val page = route(request).get

      status(page) must equalTo(BAD_REQUEST)
      assert(!AuthAction.isAuthenticated(request))
    }

  }

  "receiveMessage" should {

    "not process an unauthenticated request" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.MessagingController.receiveMessage("1").url)
      val page = route(request).get

      status(page) must equalTo(SEE_OTHER)
      assert(!AuthAction.isAuthenticated(request))
    }

    "send an event stream" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MessagingController.receiveMessage("1").url).withSession(user1Data).withHeaders(user1Data)
      val page = route(request).get

      contentType(page) must beSome.which(_ == "text/event-stream")
    }

  }

  "getMoreMessages" should {

    "not process an unauthenticated request" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.MessagingController.getMoreMessages("1").url)
      val page = route(request).get

      status(page) must equalTo(SEE_OTHER)
      assert(!AuthAction.isAuthenticated(request))
    }

    "return a JSON object" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MessagingController.getMoreMessages("1").url).withSession(user1Data).withHeaders(user1Data)
      val page = route(request).get

      contentType(page) must beSome.which(_ == "application/json")
      status(page) must equalTo(OK)
    }

  }

}
