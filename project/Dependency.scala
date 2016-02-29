import sbt._
import Keys._

object Dependency {

  val akkaVersion = "2.4-M3"
  val akkaStreamVersion = "2.0-M2"
  val scalazVersion = "7.2.0-M3"
  val sprayVersion = "1.3.3"
  val json4sVersion = "3.2.10"
  val salatVersion = "1.9.9"

  def Akka = Seq(
    "com.typesafe.akka"       %% "akka-actor"                       % akkaVersion       % "compile",
    "com.typesafe.akka"       %% "akka-persistence-experimental"    % "2.4-M2"          % "compile",
    "com.typesafe.akka"       %% "akka-http-experimental"           % "1.0"             % "compile",
    "com.typesafe.akka"       %% "akka-slf4j"                       % akkaVersion       % "compile",
    "com.github.ironfish"     %% "akka-persistence-mongo-casbah"    % "0.7.6"           % "compile",
    "com.typesafe.akka"       %% "akka-testkit"                     % akkaVersion       % "test",
    "com.typesafe.akka"       %% "akka-slf4j"                       % akkaVersion       % "compile",
    "com.typesafe.akka"       %% "akka-stream-experimental"         % akkaStreamVersion % "compile",
    "com.typesafe.akka"       %% "akka-http-experimental"           % akkaStreamVersion % "compile"
  )

  def Spray = Seq(
    "io.spray"                %% "spray-can"                        % sprayVersion    % "compile",
    "io.spray"                %% "spray-client"                     % sprayVersion    % "compile",
    "io.spray"                %% "spray-json"                       % "1.3.2"         % "compile"
  )

  def Mongo = Seq(
    "com.novus"               %% "salat"                            % salatVersion    % "compile",
    "de.flapdoodle.embed"     % "de.flapdoodle.embed.mongo"         % "1.50.0"        % "test"
  )

  def Scalaz = Seq(
    "org.scalaz"              %% "scalaz-core"                      % scalazVersion   % "compile",
    "org.scalaz"              %% "scalaz-effect"                    % scalazVersion   % "compile",
    "org.scalaj"              %% "scalaj-http"                      % "1.1.5"         % "compile",
    "com.chuusai"             %% "shapeless"                        % "2.2.5"         % "compile"
  )

  def Util = Seq(
    "com.github.nscala-time"  %% "nscala-time"                      % "2.6.0"         % "compile",
    "com.typesafe"            %  "config"                           % "1.3.0"         % "compile",
    "org.slf4j"               %  "slf4j-log4j12"                    % "1.7.12"        % "compile",
    "org.scalatest"           %% "scalatest"                        % "3.0.0-M7"      % "test"
  )

  def Resolver = Seq(
    "spray repo" at "http://repo.spray.io",
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"
  )
}
