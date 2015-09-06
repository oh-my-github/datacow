package omg.datacow.github.request


import akka.actor.ActorRef
import omg.datacow.github.response.GithubResponse._

import scala.concurrent.Future
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture

trait GithubRequest {
  def getUrl: String
  def getCredential: GithubCredential
}

final case class GithubCredential(id: String, accessToken: String)

final case class GetRepositories(owner: String,
                                 credential: GithubCredential) extends GithubRequest {
  override def getUrl: String = s"https://api.github.com/users/$owner/repos"
  override def getCredential: GithubCredential = credential
}

final case class GetRepositoryLanguages(owner: String,
                                        repository: String,
                                        credential: GithubCredential) extends GithubRequest {
  override def getUrl: String = s"https://api.github.com/repos/$owner/$repository/languages"
  override def getCredential: GithubCredential = credential
}

final case class GetAPIRateLimit(credential: GithubCredential) extends GithubRequest {
  override def getUrl: String = s"https://api.github.com/rate_limit"
  override def getCredential: GithubCredential = credential
}

object GithubRequest {}

