package business.logic

import java.util.UUID

import business.domain.{Match, PotentialMatch, User}

object PotentialMatchGenerator {
  final val POTENTIAL_MAX = 32
  final val POTENTIAL_MIN = 16
  final val MAX_DISTANCE = 200
  final val MAX_ITERATIONS = 8

  /**
   * Generate a list of new potential matches for
   * @param user      The user to generate the potential matches for
   * @param requested The maximum number of potential matches to be generated
   * @return          A list of new potential matches
   */
  private[logic] def generateForUser(user: User, requested: Int): List[PotentialMatch] = {
    Stream.tabulate(MAX_ITERATIONS) { iteration =>
      User.getNear(user, MAX_DISTANCE, requested).filter { other =>
        validPotentialMatch(user, other)
      }
    }.toStream.flatten.take(requested).map { other =>
      PotentialMatch(UUID.randomUUID().toString, user._id, other._id).save()
    }.toList
  }

  private def validPotentialMatch(user: User, other: User): Boolean = {
    if(user._id == other._id) return false
    (PotentialMatch.getForUsers(user, other), Match.getMatchByUsers(user, other)) match {
      case (None, None) => true
      case _ => false
    }
  }
}
