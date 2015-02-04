package business.domain

import java.util.Date

case class Registration(sex: String, dateOfBirth: Date, preferredLocation: String, interests: List[String]) {

}