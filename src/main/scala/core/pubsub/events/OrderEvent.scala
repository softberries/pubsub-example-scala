package core.pubsub.events

import com.permutive.pubsub.consumer.decoder.MessageDecoder
import com.permutive.pubsub.producer.encoder.MessageEncoder
import core.pubsub.CirceImplicits.{pubSubDecoder, pubSubEncoder}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class OrderEvent(id: String, name: String, price: Double)

object OrderEvent {
  implicit val orderDecoder: Decoder[OrderEvent] =
    deriveDecoder[OrderEvent]
  implicit val orderEncoder: Encoder[OrderEvent] =
    deriveEncoder[OrderEvent]
  implicit lazy val pubSubMsgEncoder: MessageEncoder[OrderEvent] = pubSubEncoder[OrderEvent]
  implicit lazy val pubSubMsgDecoder: MessageDecoder[OrderEvent] = pubSubDecoder[OrderEvent]
}
