package core

import cats.effect.IO
import config.Configuration
import core.pubsub.PubSubConsumer
import core.pubsub.events.OrderEvent
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

class CoreModule(client: Client[IO], configuration: Configuration)(implicit loggerIO: Logger[IO]) {

  lazy val orderService = new OrderService()

  lazy val orderMessageConsumer: PubSubConsumer[OrderEvent] =
    PubSubConsumer.create[OrderEvent](
      client = client,
      subscriptionName = configuration.pubSub.subscriptions.orders,
      config = configuration.pubSub
    )
}
