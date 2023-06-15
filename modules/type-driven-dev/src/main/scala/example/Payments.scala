package example
import example.models.actions.MakeCardPayment
import example.models.events.CardPayed

import scala.util.Try

trait Payments {
  def makeCardPayment(req: MakeCardPayment.type): Try[CardPayed.type]
}
