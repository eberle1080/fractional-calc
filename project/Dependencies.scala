import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
  lazy val jline = "org.jline" % "jline" % "3.8.0"
  lazy val guava = "com.google.guava" % "guava" % "25.1-jre"
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
}
