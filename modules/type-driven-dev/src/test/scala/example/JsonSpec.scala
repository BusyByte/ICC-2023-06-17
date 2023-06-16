package example

import cats.implicits.catsSyntaxEitherId
import example.IdentityVerification.IdentityValidationResult
import example.models._
import example.models.events._
import io.circe.{Codec, Decoder, Encoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import io.circe.generic.semiauto.deriveCodec
import io.circe.syntax.EncoderOps
import org.scalatest.flatspec.AnyFlatSpec

import java.util.UUID

class JsonSpec extends AnyFlatSpec {

  implicit val validatedCodec = Codec
    .from(Decoder[Boolean], Encoder[Boolean])
    .iemap(_ => IdentityValidationResult.Validated.asRight[String])(_ => true)
  implicit val cIdCodec: Codec[CustomerId]   = deriveUnwrappedCodec[CustomerId]
  implicit val caCodec: Codec[CustomerAdded] = deriveCodec[CustomerAdded]

  val customerAdded =
    CustomerAdded(CustomerId(UUID.randomUUID()), IdentityValidationResult.Validated)
  "events" should "be encoded to json" in {
    val json        = customerAdded.asJson.noSpaces
    val expecteJson = s"""{"id":"${customerAdded.id.value.toString}","result":true}"""
    assertResult(expecteJson)(json)
  }
}
