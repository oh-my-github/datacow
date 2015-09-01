package io.github.omg.datacow.github.request

import akka.actor.ActorRef
import io.github.omg.datacow.github.response.GithubResponse._

import scala.concurrent.Future
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture

sealed trait GithubRequest
final case class GithubCredential(id: String, accessToken: String)
final case class GetRepositories(id: String) extends GithubRequest
final case class GetRepositoryLanguages(id: String, repos: List[String]) extends GithubRequest
final case class GetAPIRateLimit(id: String, accessToken: String) extends GithubRequest

object GithubRequest {}

