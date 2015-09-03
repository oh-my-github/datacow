package omg.datacow.github.request


import akka.actor.ActorRef
import omg.datacow.github.response.GithubResponse._

import scala.concurrent.Future
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture

sealed trait GithubRequest {
  def url: String
}
final case class GithubCredential(id: String, accessToken: String)

final case class GetRepositories(owner: String,
                                 credential: GithubCredential) extends GithubRequest {
  override def url: String = s"https://api.github.com/users/$owner/repos"
}

final case class GetRepositoryLanguages(owner: String,
                                        repository: String,
                                        credential: GithubCredential) extends GithubRequest {
  override def url: String = s"https://api.github.com/repos/$owner/$repository/languages"
}

final case class GetAPIRateLimit(credential: GithubCredential) extends GithubRequest {
  override def url: String = s"https://api.github.com/rate_limit"
}

object GithubRequest {}

