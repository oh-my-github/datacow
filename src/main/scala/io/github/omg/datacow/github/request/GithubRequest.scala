package io.github.omg.datacow.github.request

import akka.actor.ActorRef
import io.github.omg.datacow.github.response.GithubResponse._

import scala.concurrent.Future
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture

sealed trait GithubRequest[+A]
final case class GetUserRepositoryNames(id: String) extends GithubRequest[List[String]]
final case class GetRepositoryStatus(id: String, repos: List[String]) extends GithubRequest[List[String]]
final case class GetRateLimit(id: String, accessToken: String) extends GithubRequest[APIRateLimit]

object GithubRequest {
  type Requestable[A] = Coyoneda[GithubRequest, A]

  def getUserRepositoryNames(id: String): Free[Requestable, List[String]] =
    Free.liftFC(GetUserRepositoryNames(id))

  def getUserRepositoryStatus(id: String, repos: List[String]): Free[Requestable, List[String]] =
    Free.liftFC(GetRepositoryStatus(id, repos))

  def getRepositoryStatus(id: String): Free[Requestable, List[String]] =
    for {
      repos <- getUserRepositoryNames(id)
      statuses <- getUserRepositoryStatus(id, repos)
    } yield statuses

  def getAPIRateLimit(id: String, accessToken: String): Free[Requestable, APIRateLimit] =
    Free.liftFC(GetRateLimit(id, accessToken))

}

