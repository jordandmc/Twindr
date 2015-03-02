package business.domain

import business.logic.WithApplicationAndDatabase
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TokenSpec extends Specification {

  "getByID" should {

    "find an existing token" in new WithApplicationAndDatabase {
      val token = Token.getByID(token1._id)

      token != None
      token.get._id == token1._id
      token.get.userId == token1.userId
    }

    "create and find a new token" in new WithApplicationAndDatabase {
      val token = Token("tkId123", "userABC")
      token.save()
      val newToken = Token.getByID(token._id)

      newToken != None
      token._id == newToken.get._id
      token.userId == newToken.get.userId
    }

    "not find a non-existent token" in new WithApplicationAndDatabase {
      val token = Token.getByID("9876")
      token == None
    }

  }

  "getByUserId" in new WithApplicationAndDatabase {

    "find an existing token" in new WithApplicationAndDatabase {
      val token = Token.getByUserId(token1.userId)

      token != None
      token.get._id == token1._id
      token.get.userId == token1.userId
    }

    "create and find a new token" in new WithApplicationAndDatabase {
      val token = Token("tkId123", "userABC")
      token.save()
      val newToken = Token.getByUserId(token.userId)

      newToken != None
      token._id == newToken.get._id
      token.userId == newToken.get.userId
    }

    "not find a non-existent token" in new WithApplicationAndDatabase {
      val token = Token.getByUserId("9876")
      token == None
    }

  }

  "getUserFromToken" should {

    "find an existing user" in new WithApplicationAndDatabase {
      val user = Token.getUserFromToken(token1._id)

      user != None
      user.get._id == user1._id
    }

    "not find a non-existent user" in new WithApplicationAndDatabase {
      val user = Token.getUserFromToken("9876")
      user == None
    }

  }

  "isTokenValid" should {

    "be a valid token" in new WithApplicationAndDatabase {
      Token.isTokenValid(token1._id) should beTrue
    }

    "not be a valid token" in new WithApplicationAndDatabase {
      Token.isTokenValid("") should beFalse
    }

    "not be a valid token 2" in new WithApplicationAndDatabase {
      Token.isTokenValid("askldaskda2039kalsjd") should beFalse
    }

    "not be a valid token 3" in new WithApplicationAndDatabase {
      Token.isTokenValid(null) should beFalse
    }

  }

}
