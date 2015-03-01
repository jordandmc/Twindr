package business.domain

import business.logic.{GeoJSONFormatter, WithApplicationAndDatabase}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.oauth.RequestToken

import scala.util.Random

@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification {

  "getByID" should {

    "find an existing user" in new WithApplicationAndDatabase {
      val user = User.getByID(user1._id)

      user != None
      user.get._id == user1._id
    }

    "not find a non-existent user" in new WithApplicationAndDatabase {
      val user = User.getByID("test12323")
      user == None
    }

    "not find a non-existent user 2" in new WithApplicationAndDatabase {
      val user = User.getByID("")
      user == None
    }

    "not find a non-existent user 3" in new WithApplicationAndDatabase {
      val user = User.getByID(null)
      user == None
    }

    "create and find a new user" in new WithApplicationAndDatabase {
      val newUser = User("test123", RequestToken("X-Auth-Token", "124"), "test123", None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())
      newUser.save()
      val user = User.getByID(newUser._id)

      user != None
      user.get._id == newUser._id
    }

  }

  "getByID" should {

    "find an existing user" in new WithApplicationAndDatabase {
      val user = User.getByTwitterName(user1.twitterName)

      user != None
      user.get.twitterName == user1.twitterName
    }

    "not find a non-existent user" in new WithApplicationAndDatabase {
      val user = User.getByTwitterName("test12323")
      user == None
    }

    "not find a non-existent user 2" in new WithApplicationAndDatabase {
      val user = User.getByTwitterName("")
      user == None
    }

    "not find a non-existent user 3" in new WithApplicationAndDatabase {
      val user = User.getByTwitterName(null)
      user == None
    }

    "create and find a new user" in new WithApplicationAndDatabase {
      val newUser = User("test123", RequestToken("X-Auth-Token", "124"), "test123", None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())
      newUser.save()
      val user = User.getByTwitterName(newUser.twitterName)

      user != None
      user.get.twitterName == newUser.twitterName
    }

  }

}
