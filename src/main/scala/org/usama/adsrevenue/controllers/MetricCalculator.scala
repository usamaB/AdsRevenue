package org.usama.adsrevenue.controllers

import java.io.{BufferedWriter, File, FileWriter}

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.usama.adsrevenue.models.{Clicks, Impressions, Metric}

import scala.io.Source
import scala.util.Using

object MetricCalculator {

  /**
    * Entry function, calls other functions. Separation of concerns, separate IO from logic.
    * Read/Create files -> calculate metrics -> writes data. Resource Manager takes care of the files.
    */
  def generateMetrics(
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

  private def getBufferedSource(path: String) = Source.fromFile(path)

  private def getBufferedWriter(path: String) =
    new BufferedWriter(new FileWriter(new File(path)))

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

  /**
    * Map Json to Case Class
    * @tparam A Decoder type to decode with implicit decoder available
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
    * iterates over distinct non null(county_code) values so its considerably less than O(n) if there's more duplicates
    */
  def getMetrics(
      impressionsList: List[Impressions],
      clicksList: List[Clicks]
  ): List[String] = {
    val distinctImpressions =
      impressionsList.distinct.filter(imp => imp.countryCode.isDefined)
    distinctImpressions.map { impression =>
      val filteredImpressions =
        impressionsList.filter(imp =>
          imp.appId == impression.appId && imp.countryCode == impression.countryCode
        )

      val impressionCount = filteredImpressions.foldLeft(0) {
        case (count, Impressions(_, _, _, id)) => count + id.size
      }
      val filteredClicksList =
        clicksList.filter(click => click.impressionId == impression.id)
      val revenueSum = filteredClicksList.foldLeft(0.0) {
        case (sum, Clicks(_, revenue)) => sum + revenue.getOrElse(0.0)
      }

      Metric(
        impression.appId,
        impression.countryCode.getOrElse("null"),
        impressionCount,
        filteredClicksList.size,
        revenueSum
      ).asJson.spaces2
    }
  }
}
