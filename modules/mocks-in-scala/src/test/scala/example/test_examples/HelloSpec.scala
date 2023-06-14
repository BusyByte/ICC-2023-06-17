package example.test_examples

import example.Hello
import org.scalatest.funsuite.AnyFunSuite

class HelloSpec extends AnyFunSuite {
  test("say hello") {
    assertResult(Hello.greeting)("hello")
  }
}
