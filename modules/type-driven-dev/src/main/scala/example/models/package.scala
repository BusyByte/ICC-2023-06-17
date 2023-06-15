package example

package models {

  package actions {
    sealed trait Action
    case object RegisterCustomer     extends Action
    case object AddCard              extends Action
    case object MakeCardPayment      extends Action
    case object SendCardUpgradeOffer extends Action
    case object MakeCardPurchase     extends Action
    case object SendAffiliateOffer   extends Action
  }

  package events {
    sealed trait Event
    case object CustomerAdded    extends Event
    case object CardAdded        extends Event
    case object CardPayed        extends Event
    case object CardPurchaseMade extends Event
  }
}
