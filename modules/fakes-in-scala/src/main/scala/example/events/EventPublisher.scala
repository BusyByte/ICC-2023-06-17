package example.events
import cats.effect.IO
import example.events.EventPublisher.UserCreateEventPublished
import example.logging.Logging
import example.logging.Logging.LoggingResult
import example.models.{LoggingContext, UserId}

trait EventPublisher {
  def publishUserCreated(id: UserId)(implicit lc: LoggingContext): IO[UserCreateEventPublished]
}
object EventPublisher {
  sealed trait Event {
    def logResults: List[LoggingResult]
  }
  final case class UserCreateEventPublished(id: UserId, logResults: List[LoggingResult])
      extends Event

  def impl(log: Logging): EventPublisher = new EventPublisher {
    def publishUserCreated(id: UserId)(implicit lc: LoggingContext): IO[UserCreateEventPublished] =
      for {
        lr <- log.debug("publish user created")
      } yield UserCreateEventPublished(id, List(lr))
  }
}
