import persistence.DBManager
import play.api._
import persistence.DBManager._

object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = withDB { db =>
    DBManager.createIndexes()
  }

  override def onStop(app: Application): Unit = {

  }
}
