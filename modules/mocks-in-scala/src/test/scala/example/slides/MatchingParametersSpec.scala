package example.slides

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID

object MatchingParametersSpec {

  case class UserId(value: UUID)      extends AnyVal
  case class FirstName(value: String) extends AnyVal
  case class LastName(value: String)  extends AnyVal

  case class User(id: UserId, first: FirstName, last: LastName)

  class UserRepo {
    def createNewUser(first: FirstName, last: LastName): IO[User] =
      IO.delay(User(UserId(UUID.randomUUID()), first, last))

  }

  class EventPublisher {
    def publishUserCreated(id: UserId): IO[Unit] = IO.unit
  }

  class UserService(eventPublisher: EventPublisher, userRepo: UserRepo) {
    def createUser(first: FirstName, last: LastName): IO[User] = for {
      user <- userRepo.createNewUser(first, last)
      _    <- eventPublisher.publishUserCreated(user.id)
    } yield user
  }

}

import example.slides.MatchingParametersSpec._
class MatchingParametersSpec extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  val userRepo       = mock[UserRepo]
  val eventPublisher = mock[EventPublisher]
  val service        = new UserService(eventPublisher, userRepo)

  override def beforeEach(): Unit = {
    reset(userRepo, eventPublisher)
  }

  test("doesn't like value classes") {
    val userId    = UserId(UUID.randomUUID())
    val firstName = FirstName("john")
    val lastName  = LastName("doe")

    when(userRepo.createNewUser(any[FirstName], any[LastName]))
      .thenReturn(User(userId, firstName, lastName).pure[IO])
    when(eventPublisher.publishUserCreated(any[UserId])).thenReturn(IO.unit)

    val userResult: IO[User] = service.createUser(firstName, lastName)

    userResult.attempt.unsafeRunSync() match {
      case Right(user) =>
        verify(userRepo).createNewUser(any[FirstName], any[LastName])
        verify(eventPublisher).publishUserCreated(any[UserId])
        assertResult(firstName)(user.first)
        assertResult(lastName)(user.last)
        assertResult(userId)(user.id)
    }
  }
}
