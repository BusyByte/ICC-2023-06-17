package example.events
import cats.effect.unsafe.implicits.global
import example.logging.FakeLogging
import example.models.{LoggingContext, UserId}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class EventPublisherSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  import example.scalacheck.ArbitraryInstances._

  implicit val lc: LoggingContext = LoggingContext.EMPTY
  val publisher                   = EventPublisher.impl(FakeLogging.success)

  test("should publish an event") {
    forAll { (id: UserId) =>
      val result = publisher.publishUserCreated(id).unsafeRunSync()
      assertResult(id)(result.id): Unit
      assertResult(1)(result.logResults.size)
    }
  }
}
