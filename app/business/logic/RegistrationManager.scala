package business.logic

import business.domain.{Registration, User}

object RegistrationManager {

  /**
   * Fill in profile details of a user after the first time they log in
   * @param user the user to update
   * @param info data transfer object containing profile information
   */
  def register(user: User, info: Registration): Unit = {
    user.copy(sex = Option(info.sex), dateOfBirth = Option(info.dateOfBirth), interests = createInterestList(info.interests)).save()
  }

  /**
   * Update a users interests...not that we use them for anything
   * @param user the user to update
   * @param newInterests the new list of interests
   */
  def updateProfile(user: User, newInterests: String): Unit = {
    user.copy(interests = createInterestList(newInterests)).save()
  }

  /**
   * Check to see if a user has already completed the registration step
   * @param user the user in question
   * @return true if the user has completed the registration step, false otherwise
   */
  def hasRegistered(user: User): Boolean = {
    user.sex.isDefined && user.dateOfBirth.isDefined
  }

  /**
   * Parses the interest text into a list. Assumes one interest per line
   * @param interestText The raw interest text
   * @return List of interests
   */
  private def createInterestList(interestText: String): List[String] = {
    interestText.toLowerCase.split("""\r\n|\n|\r|,( *)""").foldLeft(List[String]()) { (acc: List[String], current: String) =>
        if(! current.isEmpty) {
          current :: acc
        } else {
          acc
        }
    }
  }
}
