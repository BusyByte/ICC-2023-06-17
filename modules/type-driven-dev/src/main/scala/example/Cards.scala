package example
import scala.util.Try
import example.models.events._
import example.models.actions._
trait Cards {
  def addCard(req: AddCard): Try[CardAdded]
}
