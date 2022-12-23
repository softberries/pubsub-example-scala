package core.pubsub

import cats.effect.IO
import cats.effect.kernel.Resource
import com.permutive.pubsub.producer.PubsubProducer
import config.PubSubConfiguration
import core.pubsub.events.OrderEvent
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

class PubSubModule(client: Client[IO], pubSubConfiguration: PubSubConfiguration)(implicit logger: Logger[IO]) {
  lazy val pusSubProducerFactory = new PubSubProducerFactoryImpl(client, pubSubConfiguration)

  lazy val orderProducer: Resource[IO, PubsubProducer[IO, OrderEvent]] =
    pusSubProducerFactory.createProducer[OrderEvent](pubSubConfiguration.topics.orders)
}
