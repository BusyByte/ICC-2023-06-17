package example.slides

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID

object ArgumentCaptureSpec {

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

import example.slides.ArgumentCaptureSpec._
class ArgumentCaptureSpec extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  val userRepo       = mock[UserRepo]
  val eventPublisher = mock[EventPublisher]
  val log            = mock[Logging]
  val service        = new UserService(eventPublisher, userRepo, log)
  implicit val lc    = LoggingContext(Map())

  when(log.debug(any[String])(any[LoggingContext])).thenReturn(IO.unit)

  override def beforeEach(): Unit = {
    reset(userRepo, eventPublisher)
  }

  test("argument captures are a symptom of poor functions which don't return values") {
    val firstName = FirstName("john")
    val lastName  = LastName("doe")

    val userIdCapture: ArgumentCaptor[UUID] = ArgumentCaptor.forClass(classOf[UUID])

    when(userRepo.createNewUser(FirstName(any[String]), LastName(any[String]))(any[LoggingContext]))
      .thenReturn(User(UserId(UUID.randomUUID()), firstName, lastName).pure[IO])
    when(eventPublisher.publishUserCreated(UserId(userIdCapture.capture()))(any[LoggingContext]))
      .thenReturn(IO.unit)

    val userResult: IO[User] = service.createUser(firstName, lastName)

    userResult.attempt.unsafeRunSync() match {
      case Right(user) =>
        verify(userRepo).createNewUser(FirstName(any[String]), LastName(any[String]))(
          any[LoggingContext]
        )
        verify(eventPublisher).publishUserCreated(UserId(any[UUID]))(any[LoggingContext])
        assertResult(firstName)(user.first)
        assertResult(lastName)(user.last)
        assertResult(UserId(userIdCapture.getValue))(user.id)
    }
  }
}
