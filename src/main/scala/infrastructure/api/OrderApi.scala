package infrastructure.api

import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import com.permutive.pubsub.producer.PubsubProducer
import core.pubsub.events.OrderEvent
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.typelevel.log4cats.Logger
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.codec.newtype.{codecForNewType, schemaForNewType}

import java.util.UUID

case class OrderRequest(name: String, price: Double)
object OrderRequest {
  implicit lazy val encoder: Encoder[OrderRequest] = deriveEncoder[OrderRequest]
  implicit lazy val decoder: Decoder[OrderRequest] = deriveDecoder[OrderRequest]
}

class OrderApi(orderProducer: PubsubProducer[IO, OrderEvent])(implicit val logger: Logger[IO]) {

  lazy val endpoints = List(createOrder)

  private[this] lazy val createOrder =
    endpoint.post
      .description("Create orders")
      .in("api" / "create")
      .in(jsonBody[OrderRequest])
      .out(stringBody)
      .serverLogic(
        req =>
          logger.info(s"Creating order: $req") *>
            orderProducer
              .produce(OrderEvent(id = UUID.randomUUID().toString, name = req.name, price = req.price))
              .map(_ => "Ok".asRight[Unit])
      )
}
