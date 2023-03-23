package model.alchemy

import java.math.BigInteger
import java.text.DecimalFormat

case class EthTransaction(
  blockNumber: String,
  from: String,
  to: Option[String],
  value: String) {
  val wei = BigInt(new BigInteger(value.drop(2), 16))
  val eth = BigDecimal(wei) / BigDecimal(10000000000L)

  def format(value: BigDecimal) = {
    val format = new DecimalFormat("######.####")
    String.format("%15s", format.format(value))
  }
  def toView(ethUsd: BigDecimal): String = {

    val usd = ethUsd * eth

    (Some(s"From: $from") ++ to.map(to => s"To: $to").orElse(Some("")) ++ Some(format(eth)) ++ Some(
      format(usd))).mkString("<td>", "</td><td>", "</td>")
  }
}
