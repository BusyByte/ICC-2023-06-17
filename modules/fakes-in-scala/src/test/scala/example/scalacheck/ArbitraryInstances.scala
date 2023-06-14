package example.scalacheck
import example.models.{FirstName, LastName, LoggingContext, User, UserId}
import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary

import java.util.UUID

object ArbitraryInstances {
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
  implicit val arbUsr: Arbitrary[User] = Arbitrary {
    for {
      id    <- arbitrary[UserId]
      first <- arbitrary[FirstName]
      last  <- arbitrary[LastName]
    } yield User(id, first, last)
  }
}
