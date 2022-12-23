package utils

import core.pubsub.events.OrderEvent
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

object Generators {

  lazy val orderEventGen: Gen[OrderEvent] = for {
    id    <- Gen.uuid.map(_.toString)
    name  <- Gen.alphaStr
    price <- Gen.choose[Double](0, 100)
  } yield OrderEvent(
    id = id,
    name = name,
    price = price
  )

  implicit class GenOpt[T](gen: Gen[T]) {
    def one(): T              = gen.pureApply(Gen.Parameters.default, Seed.random(), 1000)
    def take(n: Int): List[T] = List.fill(n)(one())
  }
}
