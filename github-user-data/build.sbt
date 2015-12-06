import Path.relativeTo
import java.io.File
import scala.io.Source

name := Common.AppNameGithubUserData

version := "0.1.0"

scalaVersion := Common.ScalaVersion

libraryDependencies ++= (
  Dependency.Akka ++
    Dependency.Spray ++
    Dependency.Mongo ++
    Dependency.Scalaz ++
    Dependency.Util
)

resolvers ++= Dependency.Resolver

compile in Compile <<= (compile in Compile).dependsOn(genAppConf)

lazy val genAppConf = TaskKey[Unit]("genAppConf")

genAppConf := {
  val log = streams.value.log

  val dest = baseDirectory.value / "src/main/resources/application.conf"
  val origin = baseDirectory.value / "src/main/resources/application.conf.sample"

  dest.exists() match {
    case false =>
      log.warn(s"[$name] No application.conf")
      IO.write(dest, IO.read(origin).getBytes)
    case true => /* ignore */
  }
}

Revolver.settings
