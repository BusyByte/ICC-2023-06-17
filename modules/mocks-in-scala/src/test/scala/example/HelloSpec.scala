package example

import org.scalatest.funsuite.AnyFunSuite

class HelloSpec extends AnyFunSuite {
  test("say hello") {
    assertResult(Hello.greeting)("hello")
  }
}
