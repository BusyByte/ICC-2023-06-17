package example

import example.SmartNullPointerExceptionSpec.UserRepo
import org.mockito.Mockito.{reset, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID
import scala.util.{Failure, Success, Try}

object SmartNullPointerExceptionSpec {

  case class User(id: String, first: String, last: String)

  class UserRepo {
    def createNewUser(first: String, last: String): Try[User] = Success(
      User(UUID.randomUUID().toString, first, last)
    )
  }

  class EventPublisher {
    def publishUserCreated(id: String): Try[Unit] = Success(())
  }

  class UserService(eventPublisher: EventPublisher, userRepo: UserRepo) {
    def createUser(first: String, last: String): Try[User] = for {
      user <- userRepo.createNewUser(first, last)
      _    <- eventPublisher.publishUserCreated(user.id)
    } yield user
  }

}

import SmartNullPointerExceptionSpec._
class SmartNullPointerExceptionSpec extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  val userRepo       = mock[UserRepo]
  val eventPublisher = mock[EventPublisher]
  val service        = new UserService(eventPublisher, userRepo)

  override def beforeEach(): Unit = {
    reset(userRepo, eventPublisher)
  }

  test("fails with SmartNullPointerException even though service returns a Try") {
    val userResult: Try[User] = service.createUser("john", "doe")

    userResult match {
      case Success(user) =>
        assertResult("john")(user.first)
        assertResult("doe")(user.last)
        assertResult("id")(user.id)
      case Failure(_) => succeed
    }
  }

  test("succeeds when mocked correctly") {
    when(userRepo.createNewUser("john", "doe")).thenReturn(Success(User("1234", "john", "doe")))
    when(eventPublisher.publishUserCreated("1234")).thenReturn(Success(()))

    val userResult: Try[User] = service.createUser("john", "doe")

    userResult match {
      case Success(user) =>
        verify(userRepo).createNewUser("john", "doe")
        verify(eventPublisher).publishUserCreated("1234")
        assertResult("john")(user.first)
        assertResult("doe")(user.last)
        assertResult("1234")(user.id)
      case Failure(_) => succeed
    }
  }
}
