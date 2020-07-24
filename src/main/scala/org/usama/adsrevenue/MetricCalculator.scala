package org.usama.adsrevenue

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}
import scala.io.{BufferedSource, Source}
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import scala.util.Using

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
    countryCode: Option[String],
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
//

object MetricCalculator {

  /**
    * Entry function, calls other functions. Separation of concerns, separate IO from logic.
    * Read/Create files -> calculate metrics -> writes data. Resource Manager takes care of the files.
    */
  def caltulateMetrics(
      clicksFilePath: String,
      impressionsFilePath: String,
      outputFilePath: String
  ) = {
    Using.resources(
      getBufferedSource(clicksFilePath),
      getBufferedSource(impressionsFilePath),
      getBufferedWriter(outputFilePath)
    ) { (clicksSource, impressionsSource, bufferedWriter) =>
      val clicksData = getDecodedDataFromSource[Clicks](clicksSource)
      val impressionsData =
        getDecodedDataFromSource[Impressions](impressionsSource)

      val metrics = getMetrics(impressionsData, clicksData)
      writeSequencedData(metrics, bufferedWriter)
    }
  }

  private def getBufferedWriter(path: String) = {
    new BufferedWriter(new FileWriter(new File(path)))
  }

  /**
    * Writes data in desired format
    * [
    * data
    * ]
    */
  private def writeSequencedData[A](
      data: Seq[A],
      writer: BufferedWriter
  ): Unit = {
    writer.append('[')
    writer.newLine()
    for ((line, index) <- data.zipWithIndex) {
      writer.write(line.toString)
      if (index < data.size - 1)
        writer.append(',')
      writer.newLine()
    }
    writer.append(']')
  }

  private def getBufferedSource(path: String) = Source.fromFile(path)

  /**
    * Map Json to Case Class
    * @tparam A Decoder type to decode with impliict decoder available
    */
  def getDecodedDataFromSource[A: Decoder](
      source: Source
  ): List[A] =
    decode[List[A]](source.mkString) match {
      case Right(data) => data
      case Left(ex)    => throw new Exception(s"$ex")
    }

  /**
    * returns metrics in List[String] format String is Json String.
    * iterates over distinct values so its considerably less than O(n) if there's more duplicates
    */
  def getMetrics(
      impressionsList: List[Impressions],
      clicksList: List[Clicks]
  ): List[String] = {
    val distinctImpressions = impressionsList.distinct
    distinctImpressions.map { impression =>
      val filteredImpressions =
        impressionsList.filter(imp =>
          imp.appId == impression.appId && imp.countryCode == impression.countryCode
        )

      val impressionCount = filteredImpressions.foldLeft(0) {
        case (count, Impressions(_, _, _, id)) => count + id.size
      }
      val filteredClicksList =
        clicksList.filter(click =>
          click.impressionId == impression.id
        ) // check count for null using foldleft
      val revenueSum = filteredClicksList.foldLeft(0.0) {
        case (sum, Clicks(_, revenue)) => sum + revenue.getOrElse(0.0)
      }

      Metric(
        impression.appId,
        impression.countryCode,
        impressionCount,
        filteredClicksList.size,
        revenueSum
      ).asJson.spaces2
    }
  }
}
