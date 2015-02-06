package business.domain

import com.mongodb.casbah.Imports._
import persistence.DBManager._

trait Collected {
  def collection: String

  private[domain] def withCollection[T](block: MongoCollection => T): T = withDB { db =>
    block(db(collection))
  }
}
