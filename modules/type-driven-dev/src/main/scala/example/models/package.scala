package example

package models {
  import java.time.Instant
  import java.util.{Currency, UUID}

  final case class FirstName(value: String)   extends AnyVal
  final case class LastName(value: String)    extends AnyVal
  final case class Zip(value: String)         extends AnyVal
  final case class AddressLine(value: String) extends AnyVal
  sealed abstract class State(val name: String, val abbreviation: String)
  object State {
    case object Alabama            extends State("Alabama", "AL")
    case object Kentucky           extends State("Kentucky", "KY")
    case object Ohio               extends State("Ohio", "OH")
    case object Alaska             extends State("Alaska", "AK")
    case object Louisiana          extends State("Louisiana", "LA")
    case object Oklahoma           extends State("Oklahoma", "OK")
    case object Arizona            extends State("Arizona", "AZ")
    case object Maine              extends State("Maine", "ME")
    case object Oregon             extends State("Oregon", "OR")
    case object Arkansas           extends State("Arkansas", "AR")
    case object Maryland           extends State("Maryland", "MD")
    case object Pennsylvania       extends State("Pennsylvania", "PA")
    case object Massachusetts      extends State("Massachusetts", "MA")
    case object California         extends State("California", "CA")
    case object Michigan           extends State("Michigan", "MI")
    case object RhodeIsland        extends State("Rhode Island", "RI")
    case object Colorado           extends State("Colorado", "CO")
    case object Minnesota          extends State("Minnesota", "MN")
    case object SouthCarolina      extends State("South Carolina", "SC")
    case object Connecticut        extends State("Connecticut", "CT")
    case object Mississippi        extends State("Mississippi", "MS")
    case object SouthDakota        extends State("South Dakota", "SD")
    case object Delaware           extends State("Delaware", "DE")
    case object Missouri           extends State("Missouri", "MO")
    case object Tennessee          extends State("Tennessee", "TN")
    case object DistrictOfColumbia extends State("District of Columbia", "DC")
    case object Montana            extends State("Montana", "MT")
    case object Texas              extends State("Texas", "TX")
    case object Florida            extends State("Florida", "FL")
    case object Nebraska           extends State("Nebraska", "NE")
    case object Georgia            extends State("Georgia", "GA")
    case object Nevada             extends State("Nevada", "NV")
    case object Utah               extends State("Utah", "UT")
    case object Guam               extends State("Guam", "GU")
    case object NewHampshire       extends State("New Hampshire", "NH")
    case object Vermont            extends State("Vermont", "VT")
    case object Hawaii             extends State("Hawaii", "HI")
    case object NewJersey          extends State("New Jersey", "NJ")
    case object Virginia           extends State("Virginia", "VA")
    case object Idaho              extends State("Idaho", "ID")
    case object NewMexico          extends State("New Mexico", "NM")
    case object Illinois           extends State("Illinois", "IL")
    case object NewYork            extends State("New York", "NY")
    case object Washington         extends State("Washington", "WA")
    case object Indiana            extends State("Indiana", "IN")
    case object NorthCarolina      extends State("North Carolina", "NC")
    case object WestVirginia       extends State("West Virginia", "WV")
    case object Iowa               extends State("Iowa", "IA")
    case object NorthDakota        extends State("North Dakota", "ND")
    case object Wisconsin          extends State("Wisconsin", "WI")
    case object Kansas             extends State("Kansas", "KS")
    case object Wyoming            extends State("Wyoming", "WY")
  }
  final case class Address(lines: List[AddressLine], zip: Zip, state: State)
  final case class SSN(value: String) extends AnyVal
  final case class NewCustomer(firstName: FirstName, lastName: LastName, address: Address, ssn: SSN)
  final case class CustomerId(value: UUID) extends AnyVal
  final case class Customer(
      id: CustomerId,
      firstName: FirstName,
      lastName: LastName,
      address: Address,
      ssn: SSN
  )
  final case class CardNumber(value: String) extends AnyVal
  sealed trait CardType
  object CardType {
    object Black    extends CardType
    object Platinum extends CardType
    object Gold     extends CardType
    object Standard extends CardType
  }
  final case class Pin(value: Int) extends AnyVal
  final case class NewCard(cardType: CardType, pin: Pin)
  final case class CardId(value: UUID)        extends AnyVal
  final case class IssuedAt(value: Instant)   extends AnyVal
  final case class Expiration(value: Instant) extends AnyVal
  final case class Card(
      id: CardId,
      cardNumber: CardNumber,
      cardType: CardType,
      pin: Pin,
      issuedAt: IssuedAt,
      expiration: Expiration
  )
  final case class Amount(value: BigDecimal, currency: Currency)
  final case class AccountNumber(value: Int) extends AnyVal
  final case class RoutingNumber(value: Int) extends AnyVal
  final case class Account(accountNumber: AccountNumber, routingNumber: RoutingNumber)
  final case class PaymentRequest(amount: Amount, account: Account)
  final case class PaymentId(value: UUID)           extends AnyVal
  final case class PaymentAppliedAt(value: Instant) extends AnyVal
  final case class Payment(
      id: PaymentId,
      amount: Amount,
      account: Account,
      appliedAt: PaymentAppliedAt
  )
  final case class MerchantName(value: String) extends AnyVal
  final case class MerchantId(value: UUID)     extends AnyVal
  final case class Merchant(merchantId: MerchantId, merchantName: MerchantName)
  final case class CardPurchaseRequest(cardId: CardId, pin: Pin, total: Amount, merchant: Merchant)
  final case class PurchaseId(value: UUID) extends AnyVal
  final case class CardPurchase(
      purchaseId: PurchaseId,
      cardId: CardId,
      total: Amount,
      merchant: Merchant
  )

  final case class OfferId(value: UUID) extends AnyVal

  package actions {
    sealed trait Action
    final case class RegisterCustomer(newCustomer: NewCustomer)                     extends Action
    final case class AddCard(customerId: CustomerId, newCard: NewCard)              extends Action
    final case class MakeCardPayment(cardId: CardId, payment: PaymentRequest)       extends Action
    final case class SendCardUpgradeOffer(customerId: CustomerId, offerId: OfferId) extends Action
    final case class MakeCardPurchase(request: CardPurchaseRequest)                 extends Action
    final case class SendAffiliateOffer(customerId: CustomerId, offerId: OfferId)   extends Action
  }

  package events {
    import example.IdentityVerification.IdentityValidationResult
    sealed trait Event
    final case class CustomerAdded(id: CustomerId, result: IdentityValidationResult.Validated.type)
        extends Event
    final case class CustomerRejected(
        newCustomer: NewCustomer,
        result: IdentityValidationResult.Invalid
    ) extends Event
    final case class CardAdded(customerId: CustomerId, cardId: CardId) extends Event
    final case class CardPayed(cardId: CardId, paymentId: PaymentId)   extends Event
    final case class CardPurchaseMade(
        cardId: CardId,
        purchaseId: PurchaseId,
        valid: TransactionVerification.ValidCardPurchase.type
    ) extends Event
    final case class CardPurchaseRejected(
        request: CardPurchaseRequest,
        error: TransactionVerification.ValidationError
    ) extends Event
  }
}
