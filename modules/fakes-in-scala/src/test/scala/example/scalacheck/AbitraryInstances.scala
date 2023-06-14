package example.scalacheck
import example.models.{FirstName, LastName, LoggingContext, UserId}
import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary

import java.util.UUID

object AbitraryInstances {
  implicit val arbFN: Arbitrary[FirstName] = Arbitrary(arbitrary[String].map(FirstName.apply))
  implicit val arbLN: Arbitrary[LastName]  = Arbitrary(arbitrary[String].map(LastName.apply))
  implicit val arUID: Arbitrary[UserId] = Arbitrary(
    Gen.const(UUID.randomUUID()).map(UserId.apply)
  )
  implicit val arbLC: Arbitrary[LoggingContext] = Arbitrary(
    Gen
      .mapOf[String, String](Gen.zip(Gen.asciiPrintableStr, Gen.asciiPrintableStr))
      .map(LoggingContext.apply)
  )
}
