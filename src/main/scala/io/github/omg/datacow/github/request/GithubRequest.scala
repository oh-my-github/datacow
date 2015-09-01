package io.github.omg.datacow.github.request

import akka.actor.ActorRef
import io.github.omg.datacow.github.response.GithubResponse._

import scala.concurrent.Future
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture

sealed trait GithubRequest
final case class GithubCredential(id: String, accessToken: String)

final case class GetRepositories(owner: String,
                                 credential: GithubCredential) extends GithubRequest

final case class GetRepositoryLanguages(owner: String,
                                        repository: String,
                                        credential: GithubCredential) extends GithubRequest

final case class GetAPIRateLimit(crediential: GithubCredential) extends GithubRequest

object GithubRequest {}

