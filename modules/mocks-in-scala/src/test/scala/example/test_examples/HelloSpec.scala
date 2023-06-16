package example.test_examples

import org.scalatest.funsuite.AnyFunSuite

object HelloSpec {
  def createHelloMessage(name: String): String = s"Hello $name, nice to meet you!"
}

import HelloSpec._
class HelloSpec extends AnyFunSuite {
  test("say hello") {
    assertResult("Hello Jane Doe, nice to meet you!")(createHelloMessage("Jane Doe"))
  }
}
