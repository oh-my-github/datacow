package omg.datacow.util

import omg.datacow.github.request.GithubCredential

object TestUtility {
  val token = sys.env("GITHUB_ACCESS_TOKEN")
  val id = "1ambda"

  def getTestCredential = GithubCredential(id, token)
}
