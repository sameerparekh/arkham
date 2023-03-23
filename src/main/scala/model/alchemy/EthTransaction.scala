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
  
  def toView(ethUsd: BigDecimal): String = {

    val usd = ethUsd * eth

    (Some(from) ++ to.orElse(Some("")) ++ Some(eth) ++ Some(
      usd)).mkString("<td>", "</td><td>", "</td>")
  }
}
