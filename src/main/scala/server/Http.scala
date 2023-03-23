package server

import cats.effect.IO
import client.{AlchemyClient, CoinMarketCapClient}
import org.http4s.{Header, Headers, HttpRoutes, MediaType}
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.typelevel.ci.CIStringSyntax

import java.time.Instant

object Http {
  def makeRoutes(alchemyClient: AlchemyClient, cmcClient: CoinMarketCapClient): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root =>
        for {
          latestBlock <- alchemyClient.latestEthBlock
          ethUsd <- cmcClient.getEthUsd
          view = latestBlock.toView(ethUsd)
          now <- IO(Instant.now.getEpochSecond)
          sinceBlock = now - latestBlock.instant.getEpochSecond
          refreshTime = Math.max(1, 12 - sinceBlock)
          response <- Ok(view)
          wType =
            response
              .withContentType(`Content-Type`(new MediaType("text", "html")))
              .withHeaders(Headers(Seq(Header.Raw(ci"Refresh", refreshTime.toString))))
        } yield wType

    }
  }

}
