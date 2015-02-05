package business.logic

import business.domain.{Registration, User}

object RegistrationManager {
  def register(user: User, info: Registration): Unit = {
    User(user._id, user.oauthToken, user.twitterName, Option(info.sex), Option(info.dateOfBirth), None, createInterestList(info.interests)).save()
  }

  def hasRegistered(user: User): Boolean = {
    !user.sex.isEmpty
  }

  private def createInterestList(interestText: String): List[String] = {
    var interestList = List[String]()

    interestText.toLowerCase()
    val interests = interestText.split("""\r\n|\n|\r""")

    for(interest <- interests) {
      val temp = interest.trim()
      if(!temp.isEmpty) {
        interestList = interestList :+ temp
      }
    }

    interestList
  }
}
