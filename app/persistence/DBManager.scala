package persistence

import reactivemongo.api.{DefaultDB, MongoDriver}

object DBManager {
  type DB = DefaultDB

  private val driver = new MongoDriver
  private val connectionPool = driver.connection(List("localhost"))

  private def getConnection(name: String = "db") = connectionPool.db(name)

  def withDB[A](block: DB => A): A = {
    block(getConnection())
  }
}
