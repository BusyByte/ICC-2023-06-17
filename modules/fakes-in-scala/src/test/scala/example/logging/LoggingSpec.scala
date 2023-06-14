package example.logging
import cats.effect.unsafe.implicits.global
import example.models.LoggingContext
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class LoggingSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  import example.scalacheck.ArbitraryInstances._

  val logging = Logging.impl
  test("should log debug") {
    forAll { (msg: String, ctxt: LoggingContext) =>
      val result: Logging.LoggingResult = logging.debug(msg)(ctxt).unsafeRunSync()
      assertResult(msg)(result.message): Unit
      assertResult(None)(result.throwable): Unit
      assertResult(ctxt)(result.loggingContext)
    }
  }
}
