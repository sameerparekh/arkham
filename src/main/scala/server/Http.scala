package server

import cats.effect.IO
import client.AlchemyClient
import client.CoinMarketCapClient
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.Header
import org.http4s.Headers
import org.http4s.HttpRoutes
import org.http4s.MediaType
import org.typelevel.ci.CIStringSyntax

object Http {
  def makeRoutes(alchemyClient: AlchemyClient, cmcClient: CoinMarketCapClient): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root =>
        for {
          latestBlock <- alchemyClient.latestEthBlock
          ethUsd <- cmcClient.getEthUsd
          view = latestBlock.toView(ethUsd)
          response <- Ok(view)
          wType =
            response
              .withContentType(`Content-Type`(new MediaType("text", "html")))
              .withHeaders(Headers(Seq(Header.Raw(ci"Refresh", "12"))))
        } yield wType

    }
  }

}
