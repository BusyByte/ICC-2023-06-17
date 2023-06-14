package example.scalacheck
import example.models.{FirstName, LastName}
import org.scalacheck.Arbitrary
import Arbitrary.arbitrary

object AbitraryInstances {
  implicit val arbFN: Arbitrary[FirstName] = Arbitrary(arbitrary[String].map(FirstName.apply))
  implicit val arbLN: Arbitrary[LastName]  = Arbitrary(arbitrary[String].map(LastName.apply))
}
