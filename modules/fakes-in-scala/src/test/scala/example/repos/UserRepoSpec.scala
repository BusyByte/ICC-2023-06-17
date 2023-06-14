package example.repos
import cats.effect.unsafe.implicits.global
import example.logging.FakeLogging
import example.models.{FirstName, LastName, LoggingContext, UserId}
import example.scalacheck.ArbitraryInstances
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class UserRepoSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  import ArbitraryInstances._

  implicit val lc: LoggingContext = LoggingContext.EMPTY

  test("should return a new User") {
    forAll { (firstName: FirstName, lastName: LastName, userId: UserId) =>
      val userRepo = UserRepo.impl(FakeLogging.success, FakeUserIdProvider.fromId(userId))

      val createdUserResult: UserRepo.UserCreationResult =
        userRepo.createNewUser(firstName, lastName).unsafeRunSync()
      val createdUser = createdUserResult.user
      assertResult(firstName)(createdUser.first): Unit
      assertResult(lastName)(createdUser.last): Unit
      assertResult(userId)(createdUser.id): Unit
      assertResult(1)(createdUserResult.logResults.size)
    }
  }
}
