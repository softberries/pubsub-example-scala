package core

import cats.effect.IO
import core.pubsub.events.OrderEvent
import org.typelevel.log4cats.Logger

class OrderService()(implicit logger: Logger[IO]) {
  def consumeOrder(msg: OrderEvent): IO[Boolean] = {
    logger.info(s"Consuming order: $msg") *> IO.pure(true)
  }
}
