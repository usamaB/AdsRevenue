package org.usama.adsrevenue

import cats.implicits._
import com.monovore.decline._
import java.nio.file.Path

object Main {

  /**
    * Using decline a command-line parser built on cats
    * https://github.com/bkirwi/decline
    * Creating a command and the arguments it needs
    */
  val impressionsFilePath =
    Opts.option[Path]("impressions-file-path", "absolute path for file", "i")
  val clicksFilePath =
    Opts.option[Path]("clicks-file-path", "absolute path for file", "c")
  val outputFilePath =
    Opts
      .option[Path]("output-file-path", "absolute path for file", "o")
      .withDefault("metrics.json")

  val metricOptions =
    (impressionsFilePath, clicksFilePath, outputFilePath).tupled
  val command = Command(name = "metrics", header = "calculate ad metrics") {
    metricOptions
  }

  def main(args: Array[String]) =
    command.parse(args.toIndexedSeq) match {
      case Left(help) =>
        System.err.println(help)
        sys.exit(1)
      case Right(parsedValue) =>
        MetricCalculator.caltulateMetrics(
          parsedValue._2.toString,
          parsedValue._1.toString,
          parsedValue._3.toString
        )

    }
}
