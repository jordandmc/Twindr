package business.logic

import business.domain.{Registration, User}

object RegistrationManager {
  def register(user: User, info: Registration): Unit = {
    User(user._id, user.oauthToken, user.twitterName, Option(info.sex), Option(info.dateOfBirth), None, createInterestList(info.interests)).save()
  }

  def hasRegistered(user: User): Boolean = {
    user.sex.isDefined && user.dateOfBirth.isDefined
  }

  private def createInterestList(interestText: String): List[String] = {
    interestText.toLowerCase.split("""\r\n|\n|\r|,( +)|,| +""").foldLeft(List[String]()) { (acc: List[String], current: String) =>
        if(! current.isEmpty) {
          current :: acc
        } else {
          acc
        }
    }
  }
}
