package example.repos
import cats.effect.IO
import example.logging.Logging
import example.models.{FirstName, LastName, LoggingContext, User, UserId}

import java.util.UUID

trait UserRepo {
  def createNewUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User]
}
object UserRepo {

  def impl(log: Logging): UserRepo = new UserRepo {
    def createNewUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] =
      log.debug("creating user") >> IO.delay(
        User(UserId(UUID.randomUUID()), first, last)
      )
  }
}
