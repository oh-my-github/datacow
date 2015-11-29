name := Common.AppNameGithubUserData

version := "0.1.0"

scalaVersion := Common.ScalaVersion

val akkaVersion = "2.4-M3"
val scalazVersion = "7.2.0-M3"
val sprayVersion = "1.3.3"
val json4sVersion = "3.2.10"
val salatVersion = "1.9.9"
val scalazContribVersion = "0.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka"       %% "akka-actor"                       % akkaVersion     % "compile",
  "com.typesafe.akka"       %% "akka-persistence-experimental"    % "2.4-M2"        % "compile",
  "com.typesafe.akka"       %% "akka-http-experimental"           % "1.0"           % "compile",
  "com.typesafe.akka"       %% "akka-slf4j"                       % akkaVersion     % "compile",
  "com.typesafe"            %  "config"                           % "1.3.0"         % "compile",
  "com.github.ironfish"     %% "akka-persistence-mongo-casbah"    % "0.7.6"         % "compile",
  "org.scalaz"              %% "scalaz-core"                      % scalazVersion   % "compile",
  "org.scalaz"              %% "scalaz-effect"                    % scalazVersion   % "compile",
  "org.scalaj"              %% "scalaj-http"                      % "1.1.5"         % "compile",
  "com.novus"               %% "salat"                            % salatVersion    % "compile",
  "io.spray"                %% "spray-can"                        % sprayVersion    % "compile",
  "io.spray"                %% "spray-client"                     % sprayVersion    % "compile",
  "io.spray"                %% "spray-json"                       % "1.3.2"         % "compile",
  "org.slf4j"               %  "slf4j-log4j12"                    % "1.7.12"        % "compile",
  "com.chuusai"             %% "shapeless"                        % "2.2.5"         % "compile",
  "com.github.nscala-time"  %% "nscala-time"                      % "2.6.0"         % "compile",

  "org.scalatest"           %% "scalatest"                        % "3.0.0-M7"      % "test",
  "de.flapdoodle.embed"     % "de.flapdoodle.embed.mongo"         % "1.50.0"        % "test",
  "com.typesafe.akka"       %% "akka-testkit"                     % akkaVersion     % "test"
)

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"
)


Revolver.settings
