package infrastructure.api

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.http4s.{Method, Request, Status}
import org.http4s.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.tapir.server.http4s.Http4sServerInterpreter

class HealthcheckApiTest extends AnyFlatSpec with Matchers {
  "GET /healthcheck" should "return status 200 when called" in {
    //given
    implicit val ioRuntime: IORuntime = IORuntime.global
    val target                        = Http4sServerInterpreter[IO]().toRoutes(new HealthcheckApi().endpoints).orNotFound

    val request =
      Request[IO](Method.GET, uri = uri"/healthcheck")

    //when
    val result = target.run(request).unsafeRunSync()

    //then
    result.status shouldEqual Status.Ok
    result.as[String].unsafeRunSync() shouldEqual "Ok"
  }
}
