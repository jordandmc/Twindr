package business.logic

import business.domain.{Registration, User}

object RegistrationManager {
  def register(user: User, info: Registration): Unit = {
    User(user._id, user.oauthToken, user.twitterName, Option(info.sex), Option(info.dateOfBirth), None, info.interests).save()
  }
}
