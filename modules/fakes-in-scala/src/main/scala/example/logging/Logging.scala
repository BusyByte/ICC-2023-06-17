package example.logging

import cats.effect.IO
import example.logging.Logging.LoggingResult
import example.models.LoggingContext

trait Logging {
  def debug(msg: String)(implicit lc: LoggingContext): IO[LoggingResult]
}

object Logging {

  sealed trait Level
  object Level {
    case object DEBUG extends Level
    case object WARN  extends Level
    case object ERROR extends Level
  }
  final case class LoggingResult(
      message: String,
      level: Level,
      loggingContext: LoggingContext,
      throwable: Option[Throwable]
  )

  def impl: Logging = new Logging {
    def debug(msg: String)(implicit lc: LoggingContext): IO[LoggingResult] =
      IO.println(s"$msg with context $lc").as(LoggingResult(msg, Level.DEBUG, lc, None))
  }

}
