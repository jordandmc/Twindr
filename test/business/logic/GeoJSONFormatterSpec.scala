package business.logic

import com.mongodb.casbah.commons.MongoDBObject
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._

@RunWith(classOf[JUnitRunner])
class GeoJSONFormatterSpec extends Specification {

  private final val LONGITUDE = 10.11
  private final val LATITUDE = 20.22
  private final val GEO = MongoDBObject("type" -> "Point", "coordinates" -> List(LONGITUDE, LATITUDE))

  "GeoJSONFormatter" should {
    "Correctly generate GeoJSON strings" in {
      val formatted = GeoJSONFormatter.generateFromCoords(LONGITUDE, LATITUDE)

      formatted must be equalTo GEO
    }

    "Correctly extract coordinates from a GeoJSON string" in {
      val extracted = GeoJSONFormatter.getCoords(GEO)

      extracted must be equalTo (LONGITUDE, LATITUDE)
    }
  }
}
