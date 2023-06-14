package example.repos
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import example.models.{FirstName, LastName, LoggingContext, User}
import example.repos.UserRepo.UserCreationResult
import example.testException

object FakeUserRepo {
  def fromUser(user: User): UserRepo = new UserRepo {
    override def createNewUser(
        first: FirstName,
        last: LastName
    )(implicit
        lc: LoggingContext
    ): IO[UserCreationResult] = UserCreationResult(user, Nil).pure[IO]
  }

  val failing = new UserRepo {
    override def createNewUser(
        first: FirstName,
        last: LastName
    )(implicit
        lc: LoggingContext
    ): IO[UserCreationResult] = IO.raiseError(testException)
  }
}
