package core.pubsub

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.permutive.pubsub.consumer.ConsumerRecord
import core.pubsub.CirceImplicits._
import core.pubsub.events.OrderEvent
import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpecLike
import org.typelevel.log4cats.slf4j.Slf4jLogger
import utils.Generators._

class PubSubConsumerTest extends AnyFlatSpecLike with MockFactory with Matchers {
  implicit val logger = Slf4jLogger.getLogger[IO]
  "consumePubSubMessages" should "ack messages for which consumption recipe returns true" in new Fixture {
    //given
    val target =
      new PubSubConsumer[OrderEvent](
        pubSubSubscriber = pubSubSubscriber,
        subscriptionName
      )
    (pubSubSubscriber.subscribe _).when(*).returns(fs2.Stream.emit(consumerRecord))
    (() => consumerRecord.ack).when().returns(IO.unit)
    (() => consumerRecord.value).when().returns(orderEvent)

    //when
    target.consumePubSubMessages(_ => IO.pure(true)).compile.drain.unsafeRunSync()

    //then
    (() => consumerRecord.ack).verify()
  }

  it should "nack messages for which consumption recipe returns false" in new Fixture {
    //given
    val target =
      new PubSubConsumer[OrderEvent](
        pubSubSubscriber = pubSubSubscriber,
        subscriptionName
      )

    (pubSubSubscriber.subscribe _).when(*).returns(fs2.Stream.emit(consumerRecord))
    (() => consumerRecord.nack).when().returns(IO.unit)
    (() => consumerRecord.value).when().returns(orderEvent)

    //when
    target.consumePubSubMessages(_ => IO.pure(false)).compile.drain.unsafeRunSync()

    //then
    (() => consumerRecord.nack).verify()
  }

  private[this] trait Fixture {
    val subscriptionName = "testSubscription"

    val orderEvent     = orderEventGen.one()
    val consumerRecord = stub[ConsumerRecord[IO, OrderEvent]]

    val pubSubSubscriber = stub[PubSubSubscriber[OrderEvent]]
  }
}
