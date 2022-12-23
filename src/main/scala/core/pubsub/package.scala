package core

import cats.effect.IO
import com.permutive.pubsub.consumer.ConsumerRecord
import com.permutive.pubsub.consumer.decoder.MessageDecoder
import com.permutive.pubsub.producer.encoder.MessageEncoder
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}

import java.nio.charset.StandardCharsets
import scala.util.Try

package object pubsub {
  type PubSubStream[T] = fs2.Stream[IO, ConsumerRecord[IO, T]]

  object CirceImplicits {
    implicit def pubSubEncoder[A](implicit e: Encoder[A]): MessageEncoder[A] = (msg: A) =>
      Try(msg.asJson.spaces2.getBytes(StandardCharsets.UTF_8)).toEither

    implicit def pubSubDecoder[A](implicit d: Decoder[A]): MessageDecoder[A] = (bytes: Array[Byte]) =>
      parser.parse(new String(bytes, StandardCharsets.UTF_8)).flatMap(_.as[A])
  }
}
