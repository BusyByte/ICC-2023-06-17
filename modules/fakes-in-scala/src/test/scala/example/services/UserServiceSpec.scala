package example.services
import cats.effect.unsafe.implicits.global
import example.events.FakeEventPublisher
import example.logging.FakeLogging
import example.models.{LoggingContext, User}
import example.repos.FakeUserRepo
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import example.events.EventPublisher.UserCreateEventPublished

class UserServiceSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with Inside {
  import example.scalacheck.ArbitraryInstances._

  implicit val lc: LoggingContext = LoggingContext.EMPTY

  test("should return a new User") {
    forAll { (user: User) =>
      val service = UserService.impl(
        FakeEventPublisher.success,
        FakeUserRepo.fromUser(user),
        FakeLogging.success
      )
      val result = service.createUser(user.first, user.last).unsafeRunSync()
      assertResult(user)(result.user): Unit
      inside(result.events) { case UserCreateEventPublished(id, _) :: Nil =>
        assertResult(user.id)(id): Unit
      }
      assertResult(2)(result.logResults.size)
    }
  }
}
