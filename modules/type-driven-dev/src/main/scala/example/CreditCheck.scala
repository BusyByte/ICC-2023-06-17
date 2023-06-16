package example
import example.CreditCheck.{CreditCheckResult, FICOScoreCategory}
import example.models.{Customer, NewCustomer}

import scala.util.Try

trait CreditCheck {
  def checkNewCustomerCredit(
      newCustomer: NewCustomer,
      minimum: FICOScoreCategory
  ): Try[CreditCheckResult]
  def checkCustomerCredit(customer: Customer, minimum: FICOScoreCategory): Try[CreditCheckResult]
}
object CreditCheck {
  // range from from 300 to 850
  final case class FICOScore(value: Int) extends AnyVal

  sealed abstract class CreditCheckResult(val ficoScore: FICOScore)
  object CreditCheckResult {
    final case class Accepted(score: FICOScore)     extends CreditCheckResult(score)
    final case class RatingTooLow(score: FICOScore) extends CreditCheckResult(score)
  }

  sealed trait FICOScoreCategory
  object FICOScoreCategory {
    case object Excellent extends FICOScoreCategory
    case object VeryGood  extends FICOScoreCategory
    case object Good      extends FICOScoreCategory
    case object Fair      extends FICOScoreCategory
    case object Poor      extends FICOScoreCategory

    def fromScore(score: FICOScore): FICOScoreCategory = {
      val scoreValue = score.value
      if (scoreValue >= 800 && scoreValue <= 850) {
        Excellent
      } else if (scoreValue >= 740 && scoreValue <= 799) {
        VeryGood
      } else if (scoreValue >= 670 && scoreValue <= 739) {
        Good
      } else if (scoreValue >= 580 && scoreValue <= 669) {
        Fair
      } else {
        Poor
      }
    }
  }
}
