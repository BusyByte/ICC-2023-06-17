package example
import example.models.actions.RegisterCustomer
import example.models.events.{CustomerAdded, CustomerRejected}

import scala.util.Try

trait Customers {
  def registerCustomer(req: RegisterCustomer): Try[Either[CustomerRejected, CustomerAdded]]
}
