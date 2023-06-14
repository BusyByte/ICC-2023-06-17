package example.test_examples

import cats.implicits.showInterpolator
import example.models.{FirstName, LastName}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

object HelloPropSpec {
  def createHelloMessage(fn: FirstName, ln: LastName): String =
    show"Hello $fn $ln, nice to meet you!"
}

import example.test_examples.HelloPropSpec._
class HelloPropSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  import example.scalacheck.AbitraryInstances._
  test("say hello") {
    forAll { (firstName: FirstName, lastName: LastName) =>
      val helloMessage         = createHelloMessage(firstName, lastName)
      val expectedHelloMessage = s"Hello ${firstName.value} ${lastName.value}, nice to meet you!"
      assertResult(expectedHelloMessage)(helloMessage)
    }
  }
}
