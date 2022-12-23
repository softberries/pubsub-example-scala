package core.pubsub

import cats.effect.IO
import cats.implicits.catsSyntaxApply
import com.permutive.pubsub.consumer.decoder.MessageDecoder
import config.PubSubConfiguration
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

class PubSubConsumer[T: MessageDecoder] private[pubsub] (
    pubSubSubscriber: PubSubSubscriber[T],
    subscriptionName: String
)(implicit logger: Logger[IO]) {
  def consumePubSubMessages(consumptionRecipe: T => IO[Boolean]): fs2.Stream[IO, Unit] = {
    fs2.Stream.eval(logger.info(s"Running $subscriptionName consumer")) *>
      pubSubSubscriber
        .subscribe(subscriptionName)
        .evalMap(
          msg =>
            consumptionRecipe(msg.value).flatMap(
              isSuccessful =>
                if (isSuccessful) msg.ack
                else msg.nack
            )
        )
  }
}

object PubSubConsumer {
  def create[T: MessageDecoder](client: Client[IO], subscriptionName: String, config: PubSubConfiguration)(
      implicit logger: Logger[IO]
  ): PubSubConsumer[T] =
    new PubSubConsumer[T](
      pubSubSubscriber = new PubSubSubscriberImpl[T](client = client, config = config),
      subscriptionName = subscriptionName
    )
}
