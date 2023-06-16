package example
import example.models.actions.MakeCardPurchase
import example.models.events.{CardPurchaseMade, CardPurchaseRejected}

import scala.util.Try

trait Purchases {
  def makeCardPurchase(req: MakeCardPurchase): Try[Either[CardPurchaseRejected, CardPurchaseMade]]
}
