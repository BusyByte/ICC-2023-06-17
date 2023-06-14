package example.logging
import cats.effect.IO
import example.logging.Logging.{Level, LoggingResult}
import example.{models, testException}

object FakeLogging {
  val success = new Logging {
    override def debug(msg: String)(implicit
        lc: models.LoggingContext
    ): IO[LoggingResult] = IO.unit.as(LoggingResult(msg, Level.DEBUG, lc, None))
  }

  val failure = new Logging {
    override def debug(msg: String)(implicit
        lc: models.LoggingContext
    ): IO[LoggingResult] = IO.raiseError(testException)
  }
}
