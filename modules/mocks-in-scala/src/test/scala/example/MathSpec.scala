package example

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class MathSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  test("should be associative") {
    forAll { (x: Int, y: Int, z: Int) =>
      assertResult((x + y) + z)(x + (y + z))
    }
  }

  test("should be commutative") {
    forAll { (x: Int, y: Int) =>
      assertResult(x + y)(y + x)
    }
  }
}
