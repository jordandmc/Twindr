package business.domain

import java.util.Date

case class Registration(sex: String, dateOfBirth: Date, interests: String) {

}

case class UpdateRegistration(interests: String) {

}