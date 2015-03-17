package business.logic

import java.util.UUID

import business.domain.{Match, PotentialMatch, User}
import business.scaffolding.WithApplicationAndDatabase
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._
import play.api.libs.oauth.RequestToken

trait FirstUser {
  lazy val theUser = User(UUID.randomUUID().toString, RequestToken("123", "456"), "theUser", Option("M"),
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(),
    0.0).save()
}

trait SecondUser {
  lazy val theOtherUser = User(UUID.randomUUID().toString, RequestToken("123", "456"), "theOtherUser", Option("F"),
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(),
    1.0).save()
}

@RunWith(classOf[JUnitRunner])
class MatchingManagerSpec extends Specification {

  "getPotentialMatches" should {

    "Return an empty list when there are no PotentialMatches for a User" in new WithApplicationAndDatabase with FirstUser {
      MatchingManager.getPotentialMatches(theUser) must beEmpty
    }

    "Return existing PotentialMatches if any exist" in new WithApplicationAndDatabase with FirstUser with SecondUser {
      val thePotentialMatch = PotentialMatch(UUID.randomUUID().toString, theUser._id, theOtherUser._id).save()

      val result = MatchingManager.getPotentialMatches(theUser)
      result.size must beEqualTo(1)
      result(0).username must beEqualTo("theOtherUser")
    }

    "Generate new PotentialMatches if any can be generated for a User" in new WithApplicationAndDatabase with FirstUser with SecondUser {
      theOtherUser.save() //lazy val won't be instantiated unless we reference it
      val result = MatchingManager.getPotentialMatches(theUser)
      result.size must beEqualTo(1)
      result(0).username must beEqualTo("theOtherUser")
    }

    "Not return PotentialMatches the User has already responded to" in new WithApplicationAndDatabase with FirstUser with SecondUser {
      val thePotentialMatch = PotentialMatch(UUID.randomUUID().toString, theUser._id, theOtherUser._id, PotentialMatch.ACCEPTED).save()
      MatchingManager.getPotentialMatches(theUser) must beEmpty
    }

    "Not return PotentialMatches for which the other user has rejected" in new WithApplicationAndDatabase with FirstUser with SecondUser {
      val thePotentialMatch = PotentialMatch(UUID.randomUUID().toString, theUser._id, theOtherUser._id, PotentialMatch.PENDING, PotentialMatch.REJECTED).save()
      MatchingManager.getPotentialMatches(theUser) must beEmpty
    }

    "Not generate a new PotentialMatch if the users are already matches" in new WithApplicationAndDatabase with FirstUser with SecondUser {
      val theMatch = Match(UUID.randomUUID().toString, theUser._id, theOtherUser._id).save()
      MatchingManager.getPotentialMatches(theUser) must beEmpty
    }

  }
}
