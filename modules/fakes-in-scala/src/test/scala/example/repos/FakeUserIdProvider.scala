package example.repos
import cats.effect.IO
import example.models.UserId

object FakeUserIdProvider {
  def fromId(id: UserId): UserIdProvider = new UserIdProvider {
    override def randomUserId: IO[UserId] = IO.pure(id)
  }
}
