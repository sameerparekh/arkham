import io.circe.Json

case class JsonRpcResponse(
  jsonrpc: String,
  id: Option[Int],
  result: Option[Json],
  params: Option[Json],
  method: Option[String])
