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

object ImplicitParametersSpec {

  case class LoggingContext(values: Map[String, String])
  case class UserId(value: UUID)      extends AnyVal
  case class FirstName(value: String) extends AnyVal
  case class LastName(value: String)  extends AnyVal

  case class User(id: UserId, first: FirstName, last: LastName)

  class Logging {
    def debug(msg: String)(implicit lc: LoggingContext): IO[Unit] =
      IO.println(s"$msg with context $lc")
  }

  class UserRepo(log: Logging) {
    def createNewUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] =
      log.debug("creating user") >> IO.delay(
        User(UserId(UUID.randomUUID()), first, last)
      )

  }

  class EventPublisher(log: Logging) {
    def publishUserCreated(id: UserId)(implicit lc: LoggingContext): IO[Unit] =
      log.debug("publish user created") >> IO.unit
  }

  class UserService(eventPublisher: EventPublisher, userRepo: UserRepo, log: Logging) {
    def createUser(first: FirstName, last: LastName)(implicit lc: LoggingContext): IO[User] = for {
      _    <- log.debug("beginning to create user")
      user <- userRepo.createNewUser(first, last)
      _    <- eventPublisher.publishUserCreated(user.id)
      _    <- log.debug(s"finished creating user")
    } yield user
  }

}

import example.ImplicitParametersSpec._
class ImplicitParametersSpec extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  val userRepo       = mock[UserRepo]
  val eventPublisher = mock[EventPublisher]
  val log            = mock[Logging]
  val service        = new UserService(eventPublisher, userRepo, log)
  implicit val lc    = LoggingContext(Map())

  when(log.debug(any[String])).thenReturn(IO.unit)

  override def beforeEach(): Unit = {
    reset(userRepo, eventPublisher)
  }

  test("doesn't like value implicit values") {
    val userId    = UserId(UUID.randomUUID())
    val firstName = FirstName("john")
    val lastName  = LastName("doe")

    when(userRepo.createNewUser(FirstName(any[String]), LastName(any[String])))
      .thenReturn(User(userId, firstName, lastName).pure[IO])
    when(eventPublisher.publishUserCreated(UserId(any[UUID]))).thenReturn(IO.unit)

    val userResult: IO[User] = service.createUser(firstName, lastName)

    userResult.attempt.unsafeRunSync() match {
      case Right(user) =>
        verify(userRepo).createNewUser(FirstName(any[String]), LastName(any[String]))
        verify(eventPublisher).publishUserCreated(UserId(any[UUID]))
        assertResult(firstName)(user.first)
        assertResult(lastName)(user.last)
        assertResult(userId)(user.id)
    }
  }
}
