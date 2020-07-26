//import org.scalatest.flatspec.AnyFlatSpec
//import org.scalatest.matchers.should.Matchers
//import org.usama.adsrevenue.controllers.{MetricCalculator}
//import org.usama.adsrevenue.models.{Clicks, Impressions}
//
//import scala.io.Source
//
//class MetricCalculatorTest extends AnyFlatSpec with Matchers {
//
//  "Calculated Metrics" should "be equal to the expected result" in {
//    val sampleClicks = List[Clicks](
//      Clicks(None, Some(20.0)),
//      Clicks(Some("abc"), Some(0.2)),
//      Clicks(Some("222"), None),
//      Clicks(Some(""), Some(1.0))
//    )
//
//    val sampleImpressions = List[Impressions](
//      Impressions(1, Some(1111), None, None),
//      Impressions(2, Some(2222), Some("US"), Some("222"))
//    )
//
//    val expectedResult = List(
//      """|{
//         |  "app_id" : 1,
//         |  "country_code" : null,
//         |  "impressions" : 0,
//         |  "clicks" : 1,
//         |  "revenue" : 20.0
//         |}""".stripMargin,
//      """|{
//         |  "app_id" : 2,
//         |  "country_code" : "US",
//         |  "impressions" : 1,
//         |  "clicks" : 1,
//         |  "revenue" : 0.0
//         |}""".stripMargin
//    )
//
//    val actualResult =
//      MetricCalculator.getMetrics(sampleImpressions, sampleClicks)
//    actualResult shouldBe expectedResult
//  }
//
//  "Json Decoder" should "decode clicks json into Clicks CaseClass" in {
//    val clicksJson =
//      """
//        |[
//        |{
//        |"impressionId": "1b04c706-e3d7-4f70-aac8-25635fa24250",
//        |"revenue": 1.0617394700223026
//        |}
//        |]
//        |""".stripMargin
//
//    val expectedResult = List(
//      Clicks(
//        Some("1b04c706-e3d7-4f70-aac8-25635fa24250"),
//        Some(1.0617394700223026)
//      )
//    )
//    val actualResult =
//      MetricCalculator.getDecodedDataFromSource[Clicks](
//        Source.fromString(clicksJson)
//      )
//    actualResult shouldBe expectedResult
//  }
//
//  "Json Decoder" should "decode impressions json into Impressions CaseClass" in {
//    val clicksJson =
//      """
//        |[
//        |{
//        |"app_id": 4,
//        |"advertiser_id": 32,
//        |"country_code": "UK",
//        |"id": "a7827ab6-c4d2-475f-ab56-3a19c3172184"
//        |}
//        |]
//        |""".stripMargin
//
//    val expectedResult = List(
//      Impressions(
//        4,
//        Some(32),
//        Some("UK"),
//        Some("a7827ab6-c4d2-475f-ab56-3a19c3172184")
//      )
//    )
//    val actualResult =
//      MetricCalculator.getDecodedDataFromSource[Impressions](
//        Source.fromString(clicksJson)
//      )
//    actualResult shouldBe expectedResult
//  }
//}
