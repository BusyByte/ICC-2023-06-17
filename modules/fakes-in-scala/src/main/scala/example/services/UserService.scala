package example.services
import cats.effect.IO
import example.events.EventPublisher
import example.logging.Logging
import example.models.{FirstName, LastName, LoggingContext, User}
import example.repos.UserRepo

trait UserService {
  def createUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User]
}
object UserService {
  def impl(eventPublisher: EventPublisher, userRepo: UserRepo, log: Logging): UserService =
    new UserService {
      def createUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] =
        for {
          _    <- log.debug("beginning to create user")
          user <- userRepo.createNewUser(first, last)
          _    <- eventPublisher.publishUserCreated(user.id)
          _    <- log.debug(s"finished creating user")
        } yield user
    }
}
