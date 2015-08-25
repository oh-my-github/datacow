package io.github.omg.datacow

import scalaz._
import Scalaz._

sealed trait GithubService[+A]
final case class GetUserRepositoryNames(id: String) extends GithubService[List[String]]
final case class GetRepositoryStatus(id: String, repos: List[String]) extends GithubService[List[String]]

sealed trait GithubRequest[A]
final case class Pure[A](a: A) extends GithubRequest[A]
final case class Fetch[A](service: GithubService[A]) extends GithubRequest[A]

object GithubRequest {
  type Requestable[A] = Coyoneda[GithubRequest, A]

  def pure[A](a: A): Free[Requestable, A] =
    Free.liftFC(Pure(a): GithubRequest[A])

  def fetch[A](service: GithubService[A]): Free[Requestable, A] =
    Free.liftFC(Fetch(service): GithubRequest[A])

  def run[A](fc: Free[Requestable, A]): A = Free.runFC(fc)(GithubRequestInterpreter)

  def getUserRepositoryNames(id: String): Free[Requestable, List[String]] =
      fetch(GetUserRepositoryNames(id))

  def getUserRepositoryStatus(id: String): Free[Requestable, List[String]] =
    for {
      repos <- fetch(GetUserRepositoryNames(id))
      statuses <- fetch(GetRepositoryStatus(id, repos))
    } yield statuses
}


object GithubRequestInterpreter extends (GithubRequest ~> Id.Id) {
  import Id._

  override def apply[A](fa: GithubRequest[A]): Id[A] = fa match {
    case Pure(a) => a
    case Fetch(service) => service match {
      case GetUserRepositoryNames(userId) =>
        println(s"get user repository nams $userId")
        List("scala", "akka", "haskell")

      case GetRepositoryStatus(userId, repos) =>
        println(s"get user repo status $userId/$repos")
        List("good", "well", "poor")
    }
  }
}
