package business.logic

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.query.Imports._

object GeoJSONFormatter {

  def generateFromCoords(longitude: Double, latitude: Double): DBObject = {
    MongoDBObject("type" -> "Point", "coordinates" -> MongoDBList(longitude, latitude))
  }

  def getCoords(coords: DBObject): (Double, Double) = {
    val geoObj = coords.as[BasicDBList]("coordinates").toList.collect {
      case c: Double => c
    }

    (geoObj(0), geoObj(1))
  }
}
