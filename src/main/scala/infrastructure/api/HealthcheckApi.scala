package infrastructure.api

import cats.effect.IO
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxEitherId}
import sttp.tapir._

class HealthcheckApi {

  lazy val endpoints = List(healthcheck)

  private[this] lazy val healthcheck = endpoint.get
    .in("healthcheck")
    .out(stringBody)
    .serverLogic(_ => "Ok".asRight[Unit].pure[IO])

}
