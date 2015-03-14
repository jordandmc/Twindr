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
      val page = route(FakeRequest(GET, controllers.routes.MessagingController.messages("twitterUser").url)).get

      status(page) must equalTo(SEE_OTHER)
      contentAsString(page) must not contain ("Messaging")
    }

    "render the messages page if signed in" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.MessagingController.messages("twitterUser").url).withSession(user1Data).withHeaders(user1Data)
      val page = route(request).get

      status(page) must equalTo(OK)
      assert(AuthAction.isAuthenticated(request))
    }

  }

}
