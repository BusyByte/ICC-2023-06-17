package example.slides

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import example.logging.Logging
import example.models.{LoggingContext, UserId}
import example.repos.UserRepo
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID

/*import example.slides.ArgumentCaptureSpec._
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
}*/
