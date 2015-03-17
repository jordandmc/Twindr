package business.domain

import business.logic.WithApplicationAndDatabase
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeAfter
import persistence.DBManager._

import java.util.Date;

@RunWith(classOf[JUnitRunner])
class MatchMessageSpec extends Specification with BeforeAfter {

  val message1 = MatchMessage("1", "123456789", "twitterUser1", "test message", new Date(123))
  val message2 = MatchMessage("2", "123456789", "twitterUser2", "another message", new Date(124))
  val message3 = MatchMessage("3", "123456789", "twitterUser1", "test", new Date(125))
  val message4 = MatchMessage("4", "123456888", "twitterUser1", "message", new Date(124))

  //Why don't these fire automatically?
  def before = {
    message1.save()
    message2.save()
    message3.save()
    message4.save()
  }

  def after = withDB { db =>
    db.getCollection(MatchMessage.collection).drop
  }

  "retrievePreviousMessage" should {
    "return an empty list with no data" in new WithApplicationAndDatabase {
      val messages = MatchMessage.retrievePreviousMessage(null, null)
      messages.isEmpty must beEqualTo(true)
    }

    "return an empty list with no date" in new WithApplicationAndDatabase {
      val messages = MatchMessage.retrievePreviousMessage("123456789", null)
      messages.isEmpty must beEqualTo(true)
    }

    "return a list with all three previous messages" in new WithApplicationAndDatabase {
      before
      val messages = MatchMessage.retrievePreviousMessage("123456789", new Date(126))

      messages.isEmpty must beEqualTo(false)
      messages.length must beEqualTo(3)
      messages(0) must beEqualTo(message1)
      messages(1) must beEqualTo(message2)
      messages(2) must beEqualTo(message3)
      after
    }

    "return a list with two of the three previous messages" in new WithApplicationAndDatabase {
      before
      val messages = MatchMessage.retrievePreviousMessage("123456789", new Date(124))

      messages.isEmpty must beEqualTo(false)
      messages.length must beEqualTo(2)
      messages(0) must beEqualTo(message1)
      messages(1) must beEqualTo(message2)
      after
    }

    "return an empty list for a non existant message transaction" in new WithApplicationAndDatabase {
      val messages = MatchMessage.retrievePreviousMessage("12345", new Date(126))
      messages.isEmpty must beEqualTo(true)
    }
  }

}