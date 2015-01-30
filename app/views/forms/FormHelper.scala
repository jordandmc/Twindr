package views.forms

import views.html.helper.FieldConstructor
import views.html.forms._

object FormHelpers {
  implicit val myFields = FieldConstructor(formTemplate.f)
}
