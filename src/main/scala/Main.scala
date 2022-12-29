import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.{toFunctorOps, toSemigroupKOps}
import config.Configuration
import core.CoreModule
import core.pubsub.PubSubModule
import infrastructure.{InfrastructureModule, InfrastructureResourceModule}
import org.http4s.blaze.server.BlazeServerBuilder
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import fs2.Stream
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.server.ServerEndpoint
import utils.{Clock, StockholmClock}

object Main extends IOApp {
  implicit val loggerIO: Logger[IO] = Slf4jLogger.getLogger[IO]
  val config: Configuration         = ConfigSource.default.loadOrThrow[Configuration]
  val clock: Clock                  = new StockholmClock

  override def run(args: List[String]): IO[ExitCode] = {
    //resource modules
    lazy val infrastructureResourceModule = new InfrastructureResourceModule(config)

    val programStream =
      for {
        client <- Stream.resource(infrastructureResourceModule.client)

        backend <- Stream.resource(infrastructureResourceModule.sttpBackendResource)

        //modules
        pubSubModule         = new PubSubModule(client, config.pubSub)
        orderProducer        <- fs2.Stream.resource(pubSubModule.orderProducer)
        infrastructureModule = new InfrastructureModule(orderProducer)
        coreModule           = new CoreModule(client, config)

        //routes
        endpoints        = (infrastructureModule.endpoints).toList
        swaggerEndpoints = SwaggerInterpreter().fromServerEndpoints[IO](endpoints, "pubsub-example-scala", "0.1")

        http4sInterpreter = Http4sServerInterpreter[IO]()

        //server initialization
        _ <- BlazeServerBuilder[IO]
              .bindHttp(config.http.port, config.http.host)
              .withHttpApp(
                (http4sInterpreter.toRoutes(endpoints.asInstanceOf[List[ServerEndpoint[Fs2Streams[IO], IO]]]) <+>
                  http4sInterpreter.toRoutes(swaggerEndpoints)).orNotFound
              )
              .serve
              .concurrently {
                //consumers initialization
                coreModule.orderMessageConsumer
                  .consumePubSubMessages(
                    coreModule.orderService.consumeOrder
                  )
                  .void
              }
      } yield ()
    programStream.compile.drain.as(ExitCode.Success)
  }
}
