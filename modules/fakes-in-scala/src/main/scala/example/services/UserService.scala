package example.services
import cats.effect.IO
import example.events.EventPublisher
import example.events.EventPublisher.Event
import example.logging.Logging
import example.logging.Logging.LoggingResult
import example.models.{FirstName, LastName, LoggingContext, User}
import example.repos.UserRepo
import example.services.UserService.UserCreationResult

trait UserService {
  def createUser(first: FirstName, last: LastName)(implicit
      lc: LoggingContext
  ): IO[UserCreationResult]
}
object UserService {
  final case class UserCreationResult(
      user: User,
      events: List[Event],
      logResults: List[LoggingResult]
  )
  def impl(eventPublisher: EventPublisher, userRepo: UserRepo, log: Logging): UserService =
    new UserService {
      def createUser(first: FirstName, last: LastName)(implicit
          lc: LoggingContext
      ): IO[UserCreationResult] =
        for {
          l1            <- log.debug("beginning to create user")
          userCrtResult <- userRepo.createNewUser(first, last)
          user = userCrtResult.user
          e1 <- eventPublisher.publishUserCreated(user.id)
          l2 <- log.debug(s"finished creating user")
        } yield UserCreationResult(
          user,
          List(e1),
          l1 :: userCrtResult.logResults ++ e1.logResults ++ List(l2)
        )
    }
}
