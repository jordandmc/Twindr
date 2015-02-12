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
   * Check to see if a user has already completed the registration step
   * @param user the user in question
   * @return true if the user has completed the registration step, false otherwise
   */
  def hasRegistered(user: User): Boolean = {
    user.sex.isDefined && user.dateOfBirth.isDefined
  }

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
