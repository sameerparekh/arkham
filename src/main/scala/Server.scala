import cats.effect.{IO, Ref}
import org.http4s.{Header, Headers, HttpRoutes, MediaType, Response}
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.typelevel.ci.CIStringSyntax

object Server {
  def makeRoutes(alchemyClient: AlchemyClient, cmcClient: CoinMarketCapClient): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root => for {
        latestBlock <- alchemyClient.latestEthBlock
        ethUsd <- cmcClient.getEthUsd
        view = latestBlock.toView(ethUsd)
        response <- Ok(view)
        wType = response
          .withContentType(`Content-Type`(new MediaType("text", "html")))
          .withHeaders(Headers(Seq(Header.Raw(ci"Refresh", "12"))))
      }
        yield wType

    }
  }

}
