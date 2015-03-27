package business.logic

import business.domain.{Match, PotentialMatch, User}
import persistence.DBManager
import persistence.DBManager._
import play.api.libs.oauth.RequestToken

object TestManager {

  /**
   * Drops the database and creates a test database with known entities
   */
  def resetDatabase = withDB { db =>
    db.dropDatabase()
    DBManager.createIndexes()

    User("123", RequestToken("123", "456"), "TwindrTest", Option("M"), Option(new java.util.Date()), None, List("food", "dragons"), List("Tweet! Tweet!"), 1.0).save()
    User("124", RequestToken("124", "457"), "TwindrTest2", Option("M"), Option(new java.util.Date()), None, List(), List("Tweet"), 1.0).save()
    User("125", RequestToken("125", "458"), "TwindrTest3", Option("M"), Option(new java.util.Date()), None, List(), List("Tweet"), 1.0).save()
    User("126", RequestToken("126", "459"), "AnotherTWINDR", None, None, None, List(), List(), 1.0).save()

    PotentialMatch("1", "123", "124", PotentialMatch.PENDING, PotentialMatch.ACCEPTED).save
    PotentialMatch("2", "123", "125", PotentialMatch.ACCEPTED, PotentialMatch.ACCEPTED).save
    Match("1", "123", "125", false).save
  }

}
