package business.domain

import business.logic.GeoJSONFormatter
import business.scaffolding.WithUsers
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.oauth.RequestToken

import scala.util.Random

@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification {

  "getByID" should {

    "find an existing user" in new WithUsers {
      val user = User.getByID(user1._id)

      user != None
      user.get._id == user1._id
    }

    "not find a non-existent user" in new WithUsers {
      val user = User.getByID("test12323")
      user == None
    }

    "not find a non-existent user 2" in new WithUsers {
      val user = User.getByID("")
      user == None
    }

    "not find a non-existent user 3" in new WithUsers {
      val user = User.getByID(null)
      user == None
    }

    "create and find a new user" in new WithUsers {
      val newUser = User("test123", RequestToken("X-Auth-Token", "124"), "test123", None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())
      newUser.save()
      val user = User.getByID(newUser._id)

      user != None
      user.get._id == newUser._id
    }

  }

  "getByID" should {

    "find an existing user" in new WithUsers {
      val user = User.getByTwitterName(user1.twitterName)

      user != None
      user.get.twitterName == user1.twitterName
    }

    "not find a non-existent user" in new WithUsers {
      val user = User.getByTwitterName("test12323")
      user == None
    }

    "not find a non-existent user 2" in new WithUsers {
      val user = User.getByTwitterName("")
      user == None
    }

    "not find a non-existent user 3" in new WithUsers {
      val user = User.getByTwitterName(null)
      user == None
    }

    "create and find a new user" in new WithUsers {
      val newUser = User("test123", RequestToken("X-Auth-Token", "124"), "test123", None, None, Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(), Random.nextDouble())
      newUser.save()
      val user = User.getByTwitterName(newUser.twitterName)

      user != None
      user.get.twitterName == newUser.twitterName
    }

  }

  "update recent tweets" should {

    "update the tweets field" in new WithUsers {
      val user = User.updateUserTweets(user1, List[String]("Tweet 1", "Another tweet"))

      user.recentTweets.isEmpty must beFalse
      user.recentTweets.contains("Tweet 1") must beTrue
      user.recentTweets.contains("Another tweet") must beTrue
    }

    "update the tweets field twice" in new WithUsers {
      var user = User.updateUserTweets(user1, List[String]("Tweet 1", "Another tweet"))
      user = User.updateUserTweets(user1, List[String]("Only one tweet"))

      user.recentTweets.isEmpty must beFalse
      user.recentTweets.size must equalTo(1)
      user.recentTweets.contains("Only one tweet") must beTrue
    }

    "contain no tweets" in new WithUsers {
      val user = User.updateUserTweets(user1, List[String]())

      user.recentTweets must beEmpty
    }

  }

  "update location" should {

    "update the location field" in new WithUsers {
      val location = Option(GeoJSONFormatter.generateFromCoords(10.0, 20.0))
      val user = User.updateUserLocation(user1, location)

      user.location must equalTo(location)
    }

    "no longer have a location" in new WithUsers {
      val user = User.updateUserLocation(user1, None)

      user.location must beNone
    }
  }

}
