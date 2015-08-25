package io.github.omg.datacow

import com.typesafe.config.{Config, ConfigFactory}

import scalaz.{Free, ~>, Id, Coyoneda}

object DataCowApp extends App {
  import GithubService._

  val id = "1ambda"

  val repos = run(getUserRepositoryNames(id))
  val statuses = run(getRepositoryStatus(id))
  val conf = ConfigFactory.load()
  val token = conf.getString("github.token")


}



