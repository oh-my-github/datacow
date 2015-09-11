package omg.datacow.util

import omg.datacow.github.request.GithubCredential

object TestUtility {
  val token = sys.env("GITHUB_TOKEN")
  val id = sys.env("GITHUB_ID")

  def getTestCredential = GithubCredential(id, token)
}
