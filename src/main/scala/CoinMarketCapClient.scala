import cats.effect.IO
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.SttpBackend
import sttp.client3.UriContext
import sttp.client3.basicRequest
import sttp.client3.circe.asJson
import io.circe.generic.auto._

case class CmcQuoteResponse(
  data: Map[String, Seq[CmcQuoteData]]
                           )

case class CmcQuoteData(
  quote: Map[String, Quote]
                   )

case class Quote(
  price: BigDecimal
                )

class CoinMarketCapClient(backend: SttpBackend[IO, Fs2Streams[IO]], apiKey: String) {
  def getEthUsd: IO[BigDecimal] = {
    val request = basicRequest
      .get(
        uri"https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest"
          .withParams("symbol" -> "eth"))
      .header("X-CMC_PRO_API_KEY", apiKey)
      .response(asJson[CmcQuoteResponse])
    for {
      response <- request.send(backend)
      cmcQuoteResponse <- IO.fromTry(response.body.toTry)
      ethUsd = cmcQuoteResponse.data.values.head.head.quote.values.head.price
    } yield ethUsd
  }

}
