package example

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID

object FinalClassesSpec {

  final case class LoggingContext(values: Map[String, String])
  final case class UserId(value: UUID)      extends AnyVal
  final case class FirstName(value: String) extends AnyVal
  final case class LastName(value: String)  extends AnyVal

  final case class User(id: UserId, first: FirstName, last: LastName)

  final class Logging {
    def debug(msg: String)(implicit lc: LoggingContext): IO[Unit] =
      IO.println(s"$msg with context $lc")
  }

  final class UserRepo(log: Logging) {
    def createNewUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] =
      log.debug("creating user") >> IO.delay(
        User(UserId(UUID.randomUUID()), first, last)
      )

  }

  final class EventPublisher(log: Logging) {
    def publishUserCreated(id: UserId)(implicit lc: LoggingContext): IO[Unit] =
      log.debug("publish user created") >> IO.unit
  }

  final class UserService(eventPublisher: EventPublisher, userRepo: UserRepo, log: Logging) {
    def createUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] = for {
      _    <- log.debug("beginning to create user")
      user <- userRepo.createNewUser(first, last)
      _    <- eventPublisher.publishUserCreated(user.id)
      _    <- log.debug(s"finished creating user")
    } yield user
  }

}

import example.FinalClassesSpec._
class FinalClassesSpec extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  val userRepo       = mock[UserRepo]
  val eventPublisher = mock[EventPublisher]
  val log            = mock[Logging]
  val service        = new UserService(eventPublisher, userRepo, log)
  implicit val lc    = LoggingContext(Map())

  when(log.debug(any[String])(any[LoggingContext])).thenReturn(IO.unit)

  override def beforeEach(): Unit = {
    reset(userRepo, eventPublisher)
  }

  test("doesn't like value final classes") {
    val userId    = UserId(UUID.randomUUID())
    val firstName = FirstName("john")
    val lastName  = LastName("doe")

    when(userRepo.createNewUser(any[FirstName], any[LastName])(any[LoggingContext]))
      .thenReturn(User(userId, firstName, lastName).pure[IO])
    when(eventPublisher.publishUserCreated(any[UserId])(any[LoggingContext])).thenReturn(IO.unit)

    val userResult: IO[User] = service.createUser(firstName, lastName)

    userResult.attempt.unsafeRunSync() match {
      case Right(user) =>
        verify(userRepo).createNewUser(any[FirstName], any[LastName])(any[LoggingContext])
        verify(eventPublisher).publishUserCreated(any[UserId])(any[LoggingContext])
        assertResult(firstName)(user.first)
        assertResult(lastName)(user.last)
        assertResult(userId)(user.id)
    }
  }
}
