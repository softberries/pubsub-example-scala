package infrastructure

import cats.effect.{IO, Resource}
import config.Configuration
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.SttpBackend
import sttp.client3.http4s.Http4sBackend

class InfrastructureResourceModule(config: Configuration) {
  //resources
  lazy val client: Resource[IO, Client[IO]] = BlazeClientBuilder[IO].resource
  lazy val sttpBackendResource: Resource[IO, SttpBackend[IO, Fs2Streams[IO]]] =
    Http4sBackend.usingDefaultBlazeClientBuilder[IO]()

}
