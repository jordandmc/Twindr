package business.logic

import java.util.{UUID, Date}

import business.domain._

object MatchingManager {

  /**
   * Get a list of potential matches to send to a user
   * New potential matches may be created
   * @param user The user requesting the potential matches
   * @return a list of potential matches
   */
  def getPotentialMatches(user: User): List[PreparedPotentialMatch] = {
    val existing = PotentialMatch.getPendingForUser(user)
    val newlyGenerated = PotentialMatchGenerator.POTENTIAL_MAX - existing.size match {
      case n: Int if n <= 0 => List[PotentialMatch]()
      case n: Int => PotentialMatchGenerator.generateForUser(user, n)
    }

    preparePotentialMatches(existing ::: newlyGenerated, user)
  }

  /**
   * Process a response from a user as to if the wish to elect to match with another user
   * @param user the user who made the decision
   * @param response the response object sent from the client
   */
  def processMatchResponse(user: User, response: PotentialMatchResponse): Unit = {
    PotentialMatch.getForUserAndMatchResponse(user, response).foreach { pm =>
      response.status match {
        case PotentialMatch.ACCEPTED =>
          val newPm = pm.accept(user).save()
          if(newPm.user1State == PotentialMatch.ACCEPTED && newPm.user2State == PotentialMatch.ACCEPTED) {
            Match(UUID.randomUUID().toString, user._id, getOtherUserId(user, pm)).save()
          }
        case PotentialMatch.REJECTED =>
          pm.reject(user).save()
        case _ =>
      }
    }
  }

  /**
   * Get all of a user's matches
   * Unmatched matches are NOT included
   * @param user the user the matches are for
   * @return a list of matches
   */
  def getMatches(user: User): List[PreparedMatch] = {
    prepareMatches(Match.getMatchesForUser(user), user)
  }

  /**
   * Unmatch two users when a user has elected to unmatch with an existing match
   * @param user the user wishing to end the match
   * @param otherUsername the twitter username of the user they wish to unmatch with
   */
  def unmatch(user: User, otherUsername: String): Unit = User.getByTwitterName(otherUsername).foreach { other =>
    Match.getMatchByUsers(user, other).foreach { m =>
      m.unmatch().save()
    }
  }

  private def preparePotentialMatches(dbObjects: List[PotentialMatch], user: User): List[PreparedPotentialMatch] =
    dbObjects.foldRight(List[PreparedPotentialMatch]()) { (current: PotentialMatch, acc: List[PreparedPotentialMatch]) =>

      User.getByID(getOtherUserId(user, current)) match {
        case Some(other: User) =>
          PreparedPotentialMatch(other.twitterName, other.recentTweets, other.sex.getOrElse("X"),
            other.dateOfBirth.getOrElse(new Date())) :: acc
        case _ => acc
      }
    }

  private def prepareMatches(dbObjects: List[Match], user: User): List[PreparedMatch] =
    dbObjects.foldRight(List[PreparedMatch]()) { (current: Match, acc: List[PreparedMatch]) =>

      User.getByID(getOtherUserId(user, current)) match {
        case Some(other: User) =>
          PreparedMatch(current._id, other.twitterName, other.sex.getOrElse("X"), other.dateOfBirth.getOrElse(new Date())) :: acc
        case _ => acc
      }
    }

  private def getOtherUserId(user: User, pm: PotentialMatch): String = {
    if(user._id == pm.user1) {
      pm.user2
    } else {
      pm.user1
    }
  }

  private def getOtherUserId(user: User, m: Match): String = {
    if(user._id == m.user1) {
      m.user2
    } else {
      m.user1
    }
  }
}
