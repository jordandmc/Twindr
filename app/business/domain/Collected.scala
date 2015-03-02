package business.domain

import com.mongodb.casbah.Imports._
import persistence.DBManager._

/**
 * Provides a higher order function for working with the mongodb collection associated with an object
 * This should be implemented by the companion object of a database object
 */
trait Collected {
  def collection: String

  private[domain] def withCollection[T](block: MongoCollection => T): T = withDB { db =>
    block(db(collection))
  }
}
