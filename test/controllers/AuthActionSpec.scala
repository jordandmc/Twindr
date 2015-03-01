package controllers

import business.logic.WithApplicationAndDatabase
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class AuthActonSpec extends Specification {

  "User" should {

    "be authenticated" in new WithApplicationAndDatabase {
      val request = FakeRequest(GET, controllers.routes.Application.index().url).withSession(user1Data).withHeaders(user1Data)
      AuthAction.isAuthenticated(request) must beEqualTo(true)
    }

    "not be authenticated" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.Application.index().url)
      AuthAction.isAuthenticated(request) must beEqualTo(false)
    }

    "not be authenticated 2" in new WithApplication {
      val request = FakeRequest(GET, controllers.routes.Application.index().url).withSession()
      AuthAction.isAuthenticated(request) must beEqualTo(false)
    }

  }

}