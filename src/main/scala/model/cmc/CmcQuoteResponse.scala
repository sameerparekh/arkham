package model.cmc

case class CmcQuoteResponse(
  data: Map[String, Seq[CmcQuoteData]])
