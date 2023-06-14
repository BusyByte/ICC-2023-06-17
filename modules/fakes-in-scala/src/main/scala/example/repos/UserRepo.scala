package example.repos
import cats.effect.IO
import example.logging.Logging
import example.logging.Logging.LoggingResult
import example.models.{FirstName, LastName, LoggingContext, User, UserId}
import example.repos.UserRepo.UserCreationResult

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
  def createNewUser(first: FirstName, last: LastName)(implicit
      lc: LoggingContext
  ): IO[UserCreationResult]
}

object UserRepo {
  final case class UserCreationResult(user: User, logResults: List[LoggingResult])
  def impl(log: Logging, idProvider: UserIdProvider): UserRepo = new UserRepo {
    def createNewUser(first: FirstName, last: LastName)(implicit
        lc: LoggingContext
    ): IO[UserCreationResult] = {
      for {
        l1 <- log.debug("creating user")
        id <- idProvider.randomUserId
      } yield UserCreationResult(User(id, first, last), List(l1))
    }
  }
}
