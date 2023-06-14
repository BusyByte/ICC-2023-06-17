package example.test_examples

import cats.implicits.showInterpolator
import example.models.{FirstName, LastName}
import org.scalatest.funsuite.AnyFunSuite

object HelloSpec {
  def createHelloMessage(fn: FirstName, ln: LastName): String =
    show"Hello $fn $ln, nice to meet you!"
}

import HelloSpec._
class HelloSpec extends AnyFunSuite {
  test("say hello") {
    val firstName    = FirstName("Jane")
    val lastName     = LastName("Doe")
    val helloMessage = createHelloMessage(firstName, lastName)
    assertResult("Hello Jane Doe, nice to meet you!")(helloMessage)
  }
}
