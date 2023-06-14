package example

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.mockito.Mockito.{reset, times, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.mockito.MockitoSugar

import java.time.Instant
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object EffectCorrectnessSpec {

  class MessagePublisher {
    def publishMessage: IO[Unit] = IO.println("something happened")
  }

  def retry(effect: IO[Unit], i: Int = 0): IO[Unit] =
    if (i < 3)
      effect.flatMap(_ => retry(effect, i + 1).delayBy(2 seconds))
    else
      IO.unit

}

import example.EffectCorrectnessSpec._
class EffectCorrectnessSpec extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  val messagePublisher = mock[MessagePublisher]

  override def beforeEach(): Unit = {
    reset(messagePublisher)
  }

  test("the effect ran 3 times for publish message but we only call the publishMessage once") {

    when(messagePublisher.publishMessage).thenReturn(
      IO.delay(println(s"publishMessage invoked at ${Instant.now()}"))
    )
    val retryPlan: IO[Unit] = retry(messagePublisher.publishMessage)

    retryPlan.unsafeRunSync() should be(())

    verify(messagePublisher, times(3)).publishMessage
  }
}
