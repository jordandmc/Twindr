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
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List("FPSs", "Platformers", "RPGs"), List(),
    0.0).save()
}

trait SecondUser {
  lazy val theOtherUser = User(UUID.randomUUID().toString, RequestToken("123", "456"), "theOtherUser", Option("F"),
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(),
    1.0).save()
}

trait DesiresFemale {
  lazy val theHeterosexualUser = User(UUID.randomUUID().toString, RequestToken("123", "456"), "theNextUser", Option("M"),
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(),
    0.0, Option("F")).save()
}

trait FemaleDesiresFemale {
  lazy val theLesbianUser = User(UUID.randomUUID().toString, RequestToken("123", "456"), "theFinalUser", Option("F"),
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List(), List(),
    0.0, Option("F")).save()
}

trait InterestingUser {
  lazy val theTerroristUser = User(UUID.randomUUID().toString, RequestToken("123", "456"), "theTerroristUser", Option("M"),
    Option(new java.util.Date()), Option(GeoJSONFormatter.generateFromCoords(0.0, 0.0)), List("C4", "RPGs", "AK47s"), List(),
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

    "Generate a new Potential match that is the correct sex based on user preference" in new WithApplicationAndDatabase with DesiresFemale with SecondUser {
      theOtherUser.save()
      val result = MatchingManager.getPotentialMatches(theHeterosexualUser)
      result.size must beEqualTo(1)
      result(0).username must beEqualTo("theOtherUser")
    }

    "Not generate a new Potential match if the other user's sex filter doesn't allow for it" in new WithApplicationAndDatabase with DesiresFemale with FemaleDesiresFemale {
      theLesbianUser.save()
      MatchingManager.getPotentialMatches(theHeterosexualUser) must beEmpty
    }

    "Not generate a new Potential match if the user's sex filter doesn't allow for it" in new WithApplicationAndDatabase with DesiresFemale with FirstUser {
      theUser.save()
      MatchingManager.getPotentialMatches(theHeterosexualUser) must beEmpty
    }

    "Sort potential matches by number of common interests" in new WithApplicationAndDatabase with FirstUser with SecondUser with InterestingUser {
      theOtherUser.save()
      theTerroristUser.save()
      val result = MatchingManager.getPotentialMatches(theUser)

      result.size must beEqualTo(2)
      result(0).username must beEqualTo("theTerroristUser")
      result(1).username must beEqualTo("theOtherUser")
    }

  }
}
