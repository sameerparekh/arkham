package model.alchemy

import java.time.Instant

case class EthBlock(
  number: String,
  transactions: Seq[EthTransaction],
  timestamp: String,
                   ) {

  val instant = Instant.ofEpochSecond(Integer.parseInt(timestamp.drop(2), 16))
  def toView(ethUsd: BigDecimal): String = {
    s"<h1>$number - $instant - $ethUsd</h1>" +
      transactions.map(_.toView(ethUsd)).mkString("<table><tr><th>From</th><th>To</th><th>ETH</th><th>USD</th><tr>", "</tr><tr>", "</tr></table>")
  }
}
