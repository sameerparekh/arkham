ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "arkham"
  )

val http4sVersion = "0.23.11"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-blaze-server",
  "org.http4s" %% "http4s-circe",
).map(_ % http4sVersion)

val circeVersion = "0.15.0-M1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
).map(_ % circeVersion)

val sttpVersion = "3.5.1"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core",
  "com.softwaremill.sttp.client3" %% "httpclient-backend-fs2",
  "com.softwaremill.sttp.client3" %% "circe",
).map(_ % sttpVersion)

libraryDependencies += "com.lihaoyi" %% "pprint" % "0.7.3"

libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"

val log4catsVersion = "2.2.0"
libraryDependencies ++= Seq(
  "org.typelevel" %% "log4cats-slf4j"
).map(_ % log4catsVersion)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11"

enablePlugins(JavaAppPackaging)

herokuAppName in Compile := "arkham-sameer"
herokuJdkVersion in Compile := "19"
