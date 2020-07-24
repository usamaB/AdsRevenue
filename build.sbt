lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "org.usama",
  scalaVersion := "2.13.3",
  test in assembly := {}
)

val scalaTest    = "org.scalatest" %% "scalatest" % "3.2.0" % Test
val circeVersion = "0.13.0"
val circeDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
val decline = "com.monovore" %% "decline" % "1.0.0" excludeAll ExclusionRule(
  "org.typelevel",
  "cats-core_2.13"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "AdsRevenue",
    libraryDependencies += scalaTest,
    libraryDependencies ++= circeDependencies,
    libraryDependencies += decline
  )

scalacOptions ++= Seq(
  "-encoding",
  "utf8",             // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)
