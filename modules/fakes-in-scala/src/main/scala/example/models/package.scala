package example
import cats.Show

import java.util.UUID

package models {
  final case class LoggingContext(values: Map[String, String])
  object LoggingContext {
    val EMPTY = LoggingContext(Map())
  }
  final case class UserId(value: UUID)      extends AnyVal
  final case class FirstName(value: String) extends AnyVal
  object FirstName {
    implicit val s: Show[FirstName] = Show.show(_.value)
  }
  final case class LastName(value: String) extends AnyVal
  object LastName {
    implicit val s: Show[LastName] = Show.show(_.value)
  }

  final case class User(id: UserId, first: FirstName, last: LastName)
}
