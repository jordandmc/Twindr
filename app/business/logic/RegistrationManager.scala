package business.logic

import business.domain.{Registration, User}

object RegistrationManager {
  def register(user: User, info: Registration): Unit = {
    User(user._id, user.oauthToken, user.twitterName, Option(info.sex), Option(info.dateOfBirth), None, createInterestList(info.interests)).save()
  }

  private def createInterestList(interestText: String): List[String] = {
    val interestList = List[String]()

    interestText.toLowerCase()
    val interests = interestText.split("""\r\n|\n|\r""")

    for(interest <- interests) {
      val temp = interest.trim()
      if(!temp.isEmpty) {
        interestList :+ temp
      }
    }

    return interestList
  }
}
