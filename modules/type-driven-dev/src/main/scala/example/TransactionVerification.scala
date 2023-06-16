package example
import example.TransactionVerification.{ValidCardPurchase, ValidationError}
import example.models.CardPurchaseRequest

import scala.util.Try

trait TransactionVerification {
  def validateCardPurchase(
      req: CardPurchaseRequest
  ): Try[Either[ValidationError, ValidCardPurchase.type]]
}
object TransactionVerification {
  case object ValidCardPurchase

  sealed trait ValidationError
  object ValidationError {
    case object Overdue              extends ValidationError
    case object CardLimitExceeded    extends ValidationError
    case object MonthlyLimitExceeded extends ValidationError
    case object Stolen               extends ValidationError
    case object Fraud                extends ValidationError
  }

}
