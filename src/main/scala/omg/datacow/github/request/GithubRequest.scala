package omg.datacow.github.request

import com.github.nscala_time.time.Imports.DateTime
import scalaz._, Scalaz._

trait GithubRequest {
  def uri: String
  def requestAt: DateTime
  def credential: GithubCredential
}

final case class GithubCredential(id: String, accessToken: String)

final case class GetUserRepositories(owner: String,
                                     credential: GithubCredential,
                                     requestAt: DateTime) extends GithubRequest {
  override def uri: String = s"https://api.github.com/users/$owner/repos"
}

final case class GetRepositoryLanguages(owner: String,
                                        credential: GithubCredential,
                                        requestAt: DateTime,
                                        repository: String) extends GithubRequest {
  override def uri: String = s"https://api.github.com/repos/$owner/$repository/languages"
}

final case class GetAPIRateLimit(credential: GithubCredential,
                                 requestAt: DateTime) extends GithubRequest {
  override def uri: String = s"https://api.github.com/rate_limit"
}

object GithubRequest {
}

