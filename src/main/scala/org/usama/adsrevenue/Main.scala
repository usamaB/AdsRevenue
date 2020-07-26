package org.usama.adsrevenue

import org.usama.adsrevenue.controllers.MetricCalculator
import org.usama.adsrevenue.utils.ParameterParser

/**
  * Main entrypoint of the application
  * calls parameters parser and MetricsCalculator
  */
object Main {
  def main(args: Array[String]) = {
    val params = ParameterParser.parse(args)
    MetricCalculator.generateMetrics(
      params.clicksPath,
      params.impressionsPath,
      params.outputPath
    )
  }
}
