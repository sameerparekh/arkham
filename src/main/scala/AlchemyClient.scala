import cats.effect.{IO, Ref}
import io.circe.generic.auto._
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.circe.asJson
import sttp.client3.{SttpBackend, UriContext, basicRequest}

class AlchemyClient(
  backend: SttpBackend[IO, Fs2Streams[IO]],
  apiKey: String,
  idRef: Ref[IO, Int]) {
  def latestEthBlock: IO[EthBlock] = {
    for {
      id <- idRef.getAndUpdate(_ + 1)
      request = basicRequest.post(uri"https://eth-mainnet.alchemyapi.io/v2/$apiKey").body(
        s"""{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["latest", true],"id":$id}""".stripMargin)
        .response(asJson[JsonRpcResponse])
      response <- request.send(backend)
      jsonRpcResponse <- IO.fromTry(response.body.toTry)
      ethBlockJson <- IO.fromTry(jsonRpcResponse.result.toRight(new RuntimeException("no result")).toTry)
      ethBlock <- IO.fromTry(ethBlockJson.as[EthBlock].toTry)
    } yield ethBlock
  }
}
