package business.domain

import business.scaffolding.WithUsers
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TokenSpec extends Specification {

  "getByID" should {

    "find an existing token" in new WithUsers {
      val token = Token.getByID(token1._id)

      token must beSome
      token.get._id must equalTo(token1._id)
      token.get.userId must equalTo(token1.userId)
    }

    "create and find a new token" in new WithUsers {
      val token = Token("tkId123", "userABC")
      token.save()
      val newToken = Token.getByID(token._id)

      newToken must beSome
      token._id must equalTo(newToken.get._id)
      token.userId must equalTo(newToken.get.userId)
    }

    "not find a non-existent token" in new WithUsers {
      val token = Token.getByID("9876")
      token must beNone
    }

  }

  "getByUserId" in new WithUsers {

    "find an existing token" in new WithUsers {
      val token = Token.getByUserId(token1.userId)

      token must beSome
      token.get._id must equalTo(token1._id)
      token.get.userId must equalTo(token1.userId)
    }

    "create and find a new token" in new WithUsers {
      val token = Token("tkId123", "userABC")
      token.save()
      val newToken = Token.getByUserId(token.userId)

      newToken must beSome
      token._id must equalTo(newToken.get._id)
      token.userId must equalTo(newToken.get.userId)
    }

    "not find a non-existent token" in new WithUsers {
      val token = Token.getByUserId("9876")
      token must beNone
    }

  }

  "getUserFromToken" should {

    "find an existing user" in new WithUsers {
      val user = Token.getUserFromToken(token1._id)

      user must beSome
      user.get._id must equalTo(user1._id)
    }

    "not find a non-existent user" in new WithUsers {
      val user = Token.getUserFromToken("9876")
      user must beNone
    }

  }

  "isTokenValid" should {

    "be a valid token" in new WithUsers {
      Token.isTokenValid(token1._id) must beTrue
    }

    "not be a valid token" in new WithUsers {
      Token.isTokenValid("") must beFalse
    }

    "not be a valid token 2" in new WithUsers {
      Token.isTokenValid("askldaskda2039kalsjd") must beFalse
    }

    "not be a valid token 3" in new WithUsers {
      Token.isTokenValid(null) must beFalse
    }

  }

}
