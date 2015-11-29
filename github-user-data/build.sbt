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

Revolver.settings
