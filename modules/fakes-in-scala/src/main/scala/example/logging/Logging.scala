package example.logging

import cats.effect.IO
import example.models.LoggingContext

trait Logging {
  def debug(msg: String)(implicit lc: LoggingContext): IO[Unit]
}

object Logging {
  def impl = new Logging {
    def debug(msg: String)(implicit lc: LoggingContext): IO[Unit] =
      IO.println(s"$msg with context $lc")
  }

}
