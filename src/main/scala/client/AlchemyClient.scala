package client

import cats.effect.IO
import cats.effect.Ref
import io.circe.generic.auto._
import model.alchemy.EthBlock
import model.alchemy.JsonRpcResponse
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.circe.asJson
import sttp.client3.SttpBackend
import sttp.client3.UriContext
import sttp.client3.basicRequest

class AlchemyClient(
  backend: SttpBackend[IO, Fs2Streams[IO]],
  apiKey: String,
  idRef: Ref[IO, Int]) {
  def latestEthBlock: IO[EthBlock] = {
    for {
      id <- idRef.getAndUpdate(_ + 1)
      request =
        basicRequest
          .post(uri"https://eth-mainnet.alchemyapi.io/v2/$apiKey").body(
            s"""{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["latest", true],"id":$id}""".stripMargin)
          .response(asJson[JsonRpcResponse])
      response <- request.send(backend)
      jsonRpcResponse <- IO.fromTry(response.body.toTry)
      ethBlockJson <-
        IO.fromTry(jsonRpcResponse.result.toRight(new RuntimeException("no result")).toTry)
      ethBlock <- IO.fromTry(ethBlockJson.as[EthBlock].toTry)
    } yield ethBlock
  }
}
