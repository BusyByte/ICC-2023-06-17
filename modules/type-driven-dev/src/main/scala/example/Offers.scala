package example
import example.models.actions.{SendAffiliateOffer, SendCardUpgradeOffer}
import example.models.events.{CardPayed, CardPurchaseMade}

import scala.util.Try

trait Offers {
  def sendUpgradeOffer(req: CardPayed): Try[Option[SendCardUpgradeOffer]]
  def sendAffiliateOffer(req: CardPurchaseMade): Try[Option[SendAffiliateOffer]]
}
