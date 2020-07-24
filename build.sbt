ThisBuild / scalaVersion := "2.13.3"
ThisBuild / organization := "com.usama"

val scalaTest    = "org.scalatest" %% "scalatest" % "3.2.0"
val circeVersion = "0.13.0"
val circeDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
val decline = "com.monovore" %% "decline" % "1.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "AdsRevenue",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= circeDependencies,
    libraryDependencies += decline
  )
