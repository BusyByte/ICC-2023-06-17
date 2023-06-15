package example
import example.models.actions.RegisterCustomer
import example.models.events.CustomerAdded

import scala.util.Try

trait Customers {
  def registerCustomer(req: RegisterCustomer.type): Try[CustomerAdded.type]
}
