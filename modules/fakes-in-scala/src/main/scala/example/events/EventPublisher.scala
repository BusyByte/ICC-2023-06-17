package example.events
import cats.effect.IO
import example.logging.Logging
import example.models.{LoggingContext, UserId}

trait EventPublisher {
  def publishUserCreated(id: UserId)(implicit lc: LoggingContext): IO[Unit]
}
object EventPublisher {
  def impl(log: Logging): EventPublisher = new EventPublisher {
    def publishUserCreated(id: UserId)(implicit lc: LoggingContext): IO[Unit] =
      log.debug("publish user created") >> IO.unit
  }
}
