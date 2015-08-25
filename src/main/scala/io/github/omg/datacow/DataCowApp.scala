package io.github.omg.datacow

import scalaz.{Free, ~>, Id, Coyoneda}

object DataCowApp extends App {
  import GithubRequest._

  val id = "1ambda"

  val repos = run(getUserRepositoryNames(id))
  val statuses = run(getUserRepositoryStatus(id))

  println(repos)
  println(statuses)
}



