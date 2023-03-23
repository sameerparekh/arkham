package model.alchemy

case class EthBlock(
  number: String,
  transactions: Seq[EthTransaction]) {
  def toView(ethUsd: BigDecimal): String = {
    s"<h1>$number</h1>" +
      transactions.map(_.toView(ethUsd)).mkString("<table><tr><th>From</th><th>To</th><th>ETH</th><th>USD</th><tr>", "</tr><tr>", "</tr></table>")
  }
}
