import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "interview",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "fraction-calc",
    libraryDependencies ++= Seq(
      jline,
      guava,
      scalaTest,
      scalacheck
    )
  )
