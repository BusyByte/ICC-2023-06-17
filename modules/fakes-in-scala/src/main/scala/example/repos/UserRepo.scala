package example.repos
import cats.effect.IO
import example.logging.Logging
import example.models.{FirstName, LastName, LoggingContext, User, UserId}

import java.util.UUID

trait UserIdProvider {
  def randomUserId: IO[UserId]
}

object UserIdProvider {
  def impl: UserIdProvider = new UserIdProvider {
    override def randomUserId: IO[UserId] = IO.delay(UUID.randomUUID()).map(UserId.apply)
  }
}

trait UserRepo {
  def createNewUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User]
}

object UserRepo {
  def impl(log: Logging, idProvider: UserIdProvider): UserRepo = new UserRepo {
    def createNewUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] = {
      for {
        _  <- log.debug("creating user")
        id <- idProvider.randomUserId
      } yield User(id, first, last)
    }
  }
}
