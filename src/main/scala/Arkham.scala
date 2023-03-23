import server.Http.makeRoutes
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import client.AlchemyClient
import client.CoinMarketCapClient
import org.http4s.blaze.server.BlazeServerBuilder
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.fs2.HttpClientFs2Backend

object Arkham extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    HttpClientFs2Backend
      .resource[IO]()
      .use { backend: SttpBackend[IO, Fs2Streams[IO] with capabilities.WebSockets] =>
        for {
          port <- IO(sys.env("PORT").toInt).handleError(_ => 8888)
          idRef <- Ref[IO].of(0)
          alchemyApiKey <- IO(sys.env("ALCHEMY_APIKEY"))
          alchemyClient = new AlchemyClient(backend, alchemyApiKey, idRef)
          cmcApiKey <- IO(sys.env("CMC_APIKEY"))
          cmcClient = new CoinMarketCapClient(backend, cmcApiKey)
          routes = makeRoutes(alchemyClient, cmcClient)
          serverFiber <-
            BlazeServerBuilder[IO]
              .bindHttp(port, "0.0.0.0")
              .withHttpApp(routes.orNotFound)
              .serve
              .compile
              .drain
              .as(ExitCode.Success).start
          exit <- serverFiber.joinWithNever
        } yield exit
      }
  }
}
