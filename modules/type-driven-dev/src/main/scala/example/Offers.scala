package example
import example.models.actions.{SendAffiliateOffer, SendCardUpgradeOffer}
import example.models.events.{CardPayed, CardPurchaseMade}

import scala.util.Try

trait Offers {
  def sendUpgradeOffer(req: CardPayed.type): Try[Option[SendCardUpgradeOffer.type]]
  def sendAffiliateOffer(req: CardPurchaseMade.type): Try[Option[SendAffiliateOffer.type]]
}
