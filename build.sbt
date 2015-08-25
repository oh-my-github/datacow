name := "datacow"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.4-M3"

val scalazVersion = "7.1.3"

val sprayVersion = "1.3.3"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0-M7",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-experimental" % "2.4-M2",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0",
  "com.typesafe" % "config" % "1.3.0",
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz" %% "scalaz-effect" % scalazVersion,
  "org.scalaj" %% "scalaj-http" % "1.1.5",
  "io.spray" %% "spray-can" % sprayVersion,
  "io.spray" %% "spray-client" % sprayVersion,
  "org.json4s" %% "json4s-native" % "3.2.10"
)

resolvers += "spray repo" at "http://repo.spray.io"

Revolver.settings