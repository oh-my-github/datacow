package omg.datacow.githubuser.util

import omg.datacow.githubuser.github.request.GithubCredential

object TestUtility {
  val token = sys.env("GITHUB_ACCESS_TOKEN")
  val id = "1ambda"

  def getTestCredential = GithubCredential(id, token)
}
