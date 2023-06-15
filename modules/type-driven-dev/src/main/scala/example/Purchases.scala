package example
import example.models.actions.MakeCardPurchase
import example.models.events.CardPurchaseMade

import scala.util.Try

trait Purchases {
  def makeCardPurchase(req: MakeCardPurchase.type): Try[CardPurchaseMade.type]
}
