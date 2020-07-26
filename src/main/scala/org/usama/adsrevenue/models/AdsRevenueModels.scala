package org.usama.adsrevenue.models

import io.circe.{Decoder, Encoder}

/**
  * Object representation for Json files and their Encoders/Decoders
  */
case class Clicks(impressionId: Option[String], revenue: Option[Double])

case class Impressions(
    appId: Int,
    advertiserId: Option[Int],
    countryCode: Option[String],
    id: Option[String]
)
case class Metric(
    appId: Int,
    countryCode: String,
    impressions: Int,
    clicks: Int,
    revenue: Double
)

object Clicks {
  implicit val decoder: Decoder[Clicks] =
    Decoder.forProduct2("impression_id", "revenue")(Clicks.apply)
}

object Impressions {
  implicit val decoder: Decoder[Impressions] =
    Decoder.forProduct4("app_id", "advertiser_id", "country_code", "id")(
      Impressions.apply
    )
}

object Metric {
  implicit val encoder: Encoder[Metric] =
    Encoder.forProduct5(
      "app_id",
      "country_code",
      "impressions",
      "clicks",
      "revenue"
    )(metric =>
      (
        metric.appId,
        metric.countryCode,
        metric.impressions,
        metric.clicks,
        metric.revenue
      )
    )
}
