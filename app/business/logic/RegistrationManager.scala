package business.logic

import business.domain.{Registration, User}

object RegistrationManager {
  def register(user: User, info: Registration): Unit = {
    User(user._id, user.oauthToken, user.twitterName, info.sex, info.dateOfBirth, info.preferredLocation, info.interests).save()
  }
}
