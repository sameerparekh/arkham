case class EthBlock(
  number: String,
  transactions: Seq[EthTransaction]) {
  def toView(ethUsd: BigDecimal): String = {
    s"<h1>$number</h1>" +
      transactions.map(_.toView(ethUsd)).mkString("<table><tr>", "</tr><tr>", "</tr></table>")
  }
}
