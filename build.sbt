ThisBuild / scalaVersion := "2.13.0"
ThisBuild / organization := "com.usama"

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
val circeVersion = "0.13.0"
val circeDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val root = (project in file("."))
.enablesPlugins(JavaAppPackaging)
.settings(
    name := "AdsRevenue",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= circeDependencies,
)
