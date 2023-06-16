package example
import cats.data.NonEmptyList
import example.IdentityVerification.IdentityValidationResult
import example.models.NewCustomer

import scala.util.Try

trait IdentityVerification {
  def validateNewCustomer(newCustomer: NewCustomer): Try[IdentityValidationResult]
}
object IdentityVerification {
  sealed trait IdentityValidationResult
  object IdentityValidationResult {
    case object Validated                             extends IdentityValidationResult
    case class Invalid(reasons: NonEmptyList[String]) extends IdentityValidationResult
  }
}
