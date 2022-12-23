package core.pubsub

import cats.effect.IO
import cats.effect.kernel.Resource
import cats.implicits.catsSyntaxOptionId
import com.permutive.pubsub.producer.{Model, PubsubProducer}
import com.permutive.pubsub.producer.encoder.MessageEncoder
import com.permutive.pubsub.producer.http.{HttpPubsubProducer, PubsubHttpProducerConfig}
import config.PubSubConfiguration
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

private[pubsub] trait PubSubProducerFactory {
  def createProducer[T: MessageEncoder](topicName: String): Resource[IO, PubsubProducer[IO, T]]
}

private[pubsub] class PubSubProducerFactoryImpl(
    client: Client[IO],
    config: PubSubConfiguration
)(implicit logger: Logger[IO]) extends PubSubProducerFactory {

  def createProducer[T: MessageEncoder](topicName: String): Resource[IO, PubsubProducer[IO, T]] =
    HttpPubsubProducer.resource[IO, T](
      projectId = Model.ProjectId(config.projectId),
      topic = Model.Topic(topicName),
      googleServiceAccountPath = config.keyFileLocation.some,
      config = PubsubHttpProducerConfig[IO](
        host = config.host,
        port = config.port,
        isEmulator = false,
        onTokenRefreshError = e => logger.error(s"Producer $topicName error - Token refresh error: $e"),
        onTokenRetriesExhausted = e =>
          logger.error(
            s"Producer $topicName error -  Token retries exhausted error: $e"
          )
      ),
      client
    )
}
