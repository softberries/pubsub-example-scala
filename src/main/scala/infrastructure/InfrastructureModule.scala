package infrastructure

import cats.effect.IO
import com.permutive.pubsub.producer.PubsubProducer
import core.pubsub.events.OrderEvent
import infrastructure.api.{HealthcheckApi, OrderApi}
import org.typelevel.log4cats.Logger
import sttp.tapir.server.ServerEndpoint

class InfrastructureModule(orderProducer: PubsubProducer[IO, OrderEvent])(implicit logger: Logger[IO]) {
  val orderApi       = new OrderApi(orderProducer)
  val healthcheckApi = new HealthcheckApi()
  //routes
  lazy val endpoints: Seq[ServerEndpoint[_, IO]] = healthcheckApi.endpoints ++ orderApi.endpoints
}
