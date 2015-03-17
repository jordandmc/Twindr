package business.scaffolding

import com.typesafe.config.ConfigFactory
import org.specs2.execute.{AsResult, Result}
import persistence.DBManager
import persistence.DBManager._
import play.api.test.{FakeApplication, WithApplication}
import play.api.{Application, Configuration, GlobalSettings}

/**
 * Allows tests to run with a fake application and the testing database.
 */
trait WithApplicationAndDatabase extends WithApplication {

  /**
   * Changes the application to use the test configuration file.
   */
  override implicit val app: FakeApplication = new FakeApplication(
    withGlobal = Some(new GlobalSettings() {
      override def configuration: Configuration = {
        Configuration(ConfigFactory.load("test.conf"))
      }

      override def onStart(app: Application): Unit = withDB { db =>
        db.dropDatabase()
      }
    })
  )

  override def around[T: AsResult](t: => T): Result = super.around {
    setup()
    try {
      t
    } finally {
      tearDown()
    }
  }

  /**
   * Create indexes in the test database
   */
  def setup(): Unit = withDB { db =>
    DBManager.createIndexes()
  }

  /**
   * Clear the test database
   */
  def tearDown(): Unit = withDB { db =>
    db.dropDatabase()
  }
}