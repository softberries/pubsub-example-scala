package core.pubsub

import cats.effect.IO
import com.permutive.pubsub.consumer.Model.{ProjectId, Subscription}
import com.permutive.pubsub.consumer.decoder.MessageDecoder
import com.permutive.pubsub.consumer.http.{PubsubHttpConsumer, PubsubHttpConsumerConfig}
import com.permutive.pubsub.consumer.ConsumerRecord
import config.PubSubConfiguration
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

trait PubSubSubscriber[T] {
  def subscribe(subscriptionName: String): fs2.Stream[IO, ConsumerRecord[IO, T]]
}
class PubSubSubscriberImpl[T: MessageDecoder](client: Client[IO], config: PubSubConfiguration)(
    implicit logger: Logger[IO]
) extends PubSubSubscriber[T] {
  def subscribe(subscriptionName: String): fs2.Stream[IO, ConsumerRecord[IO, T]] =
    PubsubHttpConsumer.subscribe[IO, T](
      projectId = ProjectId(config.projectId),
      subscription = Subscription(subscriptionName),
      serviceAccountPath = Some(config.keyFileLocation),
      config = PubsubHttpConsumerConfig[IO](
        host = config.host,
        port = config.port,
        isEmulator = false,
        onTokenRefreshError = e =>
          logger.error(
            s"Subscriber $subscriptionName error - Token refresh error: $e"
          ),
        onTokenRetriesExhausted = e =>
          logger.error(
            s"Subscriber $subscriptionName error - Token retries exhausted error: $e"
          )
      ),
      client,
      (msg, err, _, nack) =>
        logger.error(
          s"Got error: $err from subscription: $subscriptionName on message: ${msg.messageId}"
        ) *> nack
    )
}
