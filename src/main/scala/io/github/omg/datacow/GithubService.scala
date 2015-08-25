package io.github.omg.datacow

import scalaz._
import Scalaz._

sealed trait GithubService[+A]
final case class GetUserRepositoryNames(id: String) extends GithubService[List[String]]
final case class GetRepositoryStatus(id: String, repos: List[String]) extends GithubService[List[String]]

object GithubService {
  type Requestable[A] = Coyoneda[GithubService, A]

  def run[A](fc: Free[Requestable, A]): A = Free.runFC(fc)(GithubRequestInterpreter)

  def getUserRepositoryNames(id: String): Free[Requestable, List[String]] =
    Free.liftFC(GetUserRepositoryNames(id))

  def getUserRepositoryStatus(id: String, repos: List[String]): Free[Requestable, List[String]] =
    Free.liftFC(GetRepositoryStatus(id, repos))

  def getRepositoryStatus(id: String): Free[Requestable, List[String]] =
    for {
      repos <- getUserRepositoryNames(id)
      statuses <- getUserRepositoryStatus(id, repos)
    } yield statuses
}

object GithubRequestInterpreter extends (GithubService ~> Id.Id) {
  import Id._

  override def apply[A](fa: GithubService[A]): Id[A] = fa match {
    case GetUserRepositoryNames(userId) =>
      println(s"get user repository nams $userId")
      List("scala", "akka", "haskell")

    case GetRepositoryStatus(userId, repos) =>
      println(s"get user repo status $userId/$repos")
      List("good", "well", "poor")
  }
}
