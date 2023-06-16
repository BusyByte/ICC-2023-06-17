package example

import cats.Show
import cats.implicits._
import example.models._
import org.scalacheck.Arbitrary
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.util.UUID

class ArbitrarySpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  implicit val showCID: Show[CustomerId] = Show[UUID].contramap(_.value)
  implicit val arbCID: Arbitrary[CustomerId] = Arbitrary(
    Arbitrary.arbitrary[UUID].map(CustomerId.apply)
  )

  "customerId" should "be generated with arbitrary and show with show" in {
    forAll { customerId: CustomerId =>
      assertResult(customerId.value.toString)(customerId.show)
    }
  }
}
