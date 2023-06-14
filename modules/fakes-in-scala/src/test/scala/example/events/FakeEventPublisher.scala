package example.events
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import example.events.EventPublisher.UserCreateEventPublished
import example.models.{LoggingContext, UserId}
import example.testException

object FakeEventPublisher {
  val success = new EventPublisher {
    override def publishUserCreated(id: UserId)(implicit
        lc: LoggingContext
    ): IO[UserCreateEventPublished] = UserCreateEventPublished(id, List()).pure[IO]
  }

  val failing = new EventPublisher {
    override def publishUserCreated(id: UserId)(implicit
        lc: LoggingContext
    ): IO[UserCreateEventPublished] = IO.raiseError(testException)
  }
}
