package io.github.omg.datacow.github.response

import org.scalatest.{FunSuite, Matchers}
import spray.json._

class GithubResponseSpec extends FunSuite with Matchers {
  import GithubResponse._

  test("unmarshal /rate_limit") {
    import GithubResponse.Protocol._

    val response =
      """
        | {
        |  "resources": {
        |    "core": {
        |      "limit": 5000,
        |      "remaining": 5000,
        |      "reset": 1440690501
        |    },
        |    "search": {
        |      "limit": 30,
        |      "remaining": 30,
        |      "reset": 1440686961
        |    }
        |  },
        |  "rate": {
        |    "limit": 5000,
        |    "remaining": 5000,
        |    "reset": 1440690501
        |  }
        |}
      """.stripMargin

    val rateLimit:APIRateLimit = response.parseJson.convertTo[APIRateLimit]
    println(rateLimit)
  }

  test("unmarshal /users/:username/repos") {

  }

  test("unmarshal /users/:owner/:repo") {

  }
}

object GithubResponseSpecFixture {
  val userReposExample =
  """
    |[
    |  {
    |    "id": 34974421,
    |    "name": "7languages",
    |    "full_name": "njir/7languages",
    |    "owner": {
    |      "login": "njir",
    |      "id": 7614353,
    |      "avatar_url": "https://avatars.githubusercontent.com/u/7614353?v=3",
    |      "gravatar_id": "",
    |      "url": "https://api.github.com/users/njir",
    |      "html_url": "https://github.com/njir",
    |      "followers_url": "https://api.github.com/users/njir/followers",
    |      "following_url": "https://api.github.com/users/njir/following{/other_user}",
    |      "gists_url": "https://api.github.com/users/njir/gists{/gist_id}",
    |      "starred_url": "https://api.github.com/users/njir/starred{/owner}{/repo}",
    |      "subscriptions_url": "https://api.github.com/users/njir/subscriptions",
    |      "organizations_url": "https://api.github.com/users/njir/orgs",
    |      "repos_url": "https://api.github.com/users/njir/repos",
    |      "events_url": "https://api.github.com/users/njir/events{/privacy}",
    |      "received_events_url": "https://api.github.com/users/njir/received_events",
    |      "type": "User",
    |      "site_admin": false
    |    },
    |    "private": false,
    |    "html_url": "https://github.com/njir/7languages",
    |    "description": "seven languages in seven weeks",
    |    "fork": false,
    |    "url": "https://api.github.com/repos/njir/7languages",
    |    "forks_url": "https://api.github.com/repos/njir/7languages/forks",
    |    "keys_url": "https://api.github.com/repos/njir/7languages/keys{/key_id}",
    |    "collaborators_url": "https://api.github.com/repos/njir/7languages/collaborators{/collaborator}",
    |    "teams_url": "https://api.github.com/repos/njir/7languages/teams",
    |    "hooks_url": "https://api.github.com/repos/njir/7languages/hooks",
    |    "issue_events_url": "https://api.github.com/repos/njir/7languages/issues/events{/number}",
    |    "events_url": "https://api.github.com/repos/njir/7languages/events",
    |    "assignees_url": "https://api.github.com/repos/njir/7languages/assignees{/user}",
    |    "branches_url": "https://api.github.com/repos/njir/7languages/branches{/branch}",
    |    "tags_url": "https://api.github.com/repos/njir/7languages/tags",
    |    "blobs_url": "https://api.github.com/repos/njir/7languages/git/blobs{/sha}",
    |    "git_tags_url": "https://api.github.com/repos/njir/7languages/git/tags{/sha}",
    |    "git_refs_url": "https://api.github.com/repos/njir/7languages/git/refs{/sha}",
    |    "trees_url": "https://api.github.com/repos/njir/7languages/git/trees{/sha}",
    |    "statuses_url": "https://api.github.com/repos/njir/7languages/statuses/{sha}",
    |    "languages_url": "https://api.github.com/repos/njir/7languages/languages",
    |    "stargazers_url": "https://api.github.com/repos/njir/7languages/stargazers",
    |    "contributors_url": "https://api.github.com/repos/njir/7languages/contributors",
    |    "subscribers_url": "https://api.github.com/repos/njir/7languages/subscribers",
    |    "subscription_url": "https://api.github.com/repos/njir/7languages/subscription",
    |    "commits_url": "https://api.github.com/repos/njir/7languages/commits{/sha}",
    |    "git_commits_url": "https://api.github.com/repos/njir/7languages/git/commits{/sha}",
    |    "comments_url": "https://api.github.com/repos/njir/7languages/comments{/number}",
    |    "issue_comment_url": "https://api.github.com/repos/njir/7languages/issues/comments{/number}",
    |    "contents_url": "https://api.github.com/repos/njir/7languages/contents/{+path}",
    |    "compare_url": "https://api.github.com/repos/njir/7languages/compare/{base}...{head}",
    |    "merges_url": "https://api.github.com/repos/njir/7languages/merges",
    |    "archive_url": "https://api.github.com/repos/njir/7languages/{archive_format}{/ref}",
    |    "downloads_url": "https://api.github.com/repos/njir/7languages/downloads",
    |    "issues_url": "https://api.github.com/repos/njir/7languages/issues{/number}",
    |    "pulls_url": "https://api.github.com/repos/njir/7languages/pulls{/number}",
    |    "milestones_url": "https://api.github.com/repos/njir/7languages/milestones{/number}",
    |    "notifications_url": "https://api.github.com/repos/njir/7languages/notifications{?since,all,participating}",
    |    "labels_url": "https://api.github.com/repos/njir/7languages/labels{/name}",
    |    "releases_url": "https://api.github.com/repos/njir/7languages/releases{/id}",
    |    "created_at": "2015-05-03T05:06:14Z",
    |    "updated_at": "2015-05-10T02:48:39Z",
    |    "pushed_at": "2015-05-10T02:48:39Z",
    |    "git_url": "git://github.com/njir/7languages.git",
    |    "ssh_url": "git@github.com:njir/7languages.git",
    |    "clone_url": "https://github.com/njir/7languages.git",
    |    "svn_url": "https://github.com/njir/7languages",
    |    "homepage": null,
    |    "size": 148,
    |    "stargazers_count": 0,
    |    "watchers_count": 0,
    |    "language": "Clojure",
    |    "has_issues": true,
    |    "has_downloads": true,
    |    "has_wiki": true,
    |    "has_pages": false,
    |    "forks_count": 0,
    |    "mirror_url": null,
    |    "open_issues_count": 0,
    |    "forks": 0,
    |    "open_issues": 0,
    |    "watchers": 0,
    |    "default_branch": "master",
    |    "permissions": {
    |      "admin": false,
    |      "push": false,
    |      "pull": true
    |    }
    |  },
    |  {
    |    "id": 36213743,
    |    "name": "codeforces",
    |    "full_name": "njir/codeforces",
    |    "owner": {
    |      "login": "njir",
    |      "id": 7614353,
    |      "avatar_url": "https://avatars.githubusercontent.com/u/7614353?v=3",
    |      "gravatar_id": "",
    |      "url": "https://api.github.com/users/njir",
    |      "html_url": "https://github.com/njir",
    |      "followers_url": "https://api.github.com/users/njir/followers",
    |      "following_url": "https://api.github.com/users/njir/following{/other_user}",
    |      "gists_url": "https://api.github.com/users/njir/gists{/gist_id}",
    |      "starred_url": "https://api.github.com/users/njir/starred{/owner}{/repo}",
    |      "subscriptions_url": "https://api.github.com/users/njir/subscriptions",
    |      "organizations_url": "https://api.github.com/users/njir/orgs",
    |      "repos_url": "https://api.github.com/users/njir/repos",
    |      "events_url": "https://api.github.com/users/njir/events{/privacy}",
    |      "received_events_url": "https://api.github.com/users/njir/received_events",
    |      "type": "User",
    |      "site_admin": false
    |    },
    |    "private": false,
    |    "html_url": "https://github.com/njir/codeforces",
    |    "description": "simple c++ code to solve problemset in codeforces.com",
    |    "fork": false,
    |    "url": "https://api.github.com/repos/njir/codeforces",
    |    "forks_url": "https://api.github.com/repos/njir/codeforces/forks",
    |    "keys_url": "https://api.github.com/repos/njir/codeforces/keys{/key_id}",
    |    "collaborators_url": "https://api.github.com/repos/njir/codeforces/collaborators{/collaborator}",
    |    "teams_url": "https://api.github.com/repos/njir/codeforces/teams",
    |    "hooks_url": "https://api.github.com/repos/njir/codeforces/hooks",
    |    "issue_events_url": "https://api.github.com/repos/njir/codeforces/issues/events{/number}",
    |    "events_url": "https://api.github.com/repos/njir/codeforces/events",
    |    "assignees_url": "https://api.github.com/repos/njir/codeforces/assignees{/user}",
    |    "branches_url": "https://api.github.com/repos/njir/codeforces/branches{/branch}",
    |    "tags_url": "https://api.github.com/repos/njir/codeforces/tags",
    |    "blobs_url": "https://api.github.com/repos/njir/codeforces/git/blobs{/sha}",
    |    "git_tags_url": "https://api.github.com/repos/njir/codeforces/git/tags{/sha}",
    |    "git_refs_url": "https://api.github.com/repos/njir/codeforces/git/refs{/sha}",
    |    "trees_url": "https://api.github.com/repos/njir/codeforces/git/trees{/sha}",
    |    "statuses_url": "https://api.github.com/repos/njir/codeforces/statuses/{sha}",
    |    "languages_url": "https://api.github.com/repos/njir/codeforces/languages",
    |    "stargazers_url": "https://api.github.com/repos/njir/codeforces/stargazers",
    |    "contributors_url": "https://api.github.com/repos/njir/codeforces/contributors",
    |    "subscribers_url": "https://api.github.com/repos/njir/codeforces/subscribers",
    |    "subscription_url": "https://api.github.com/repos/njir/codeforces/subscription",
    |    "commits_url": "https://api.github.com/repos/njir/codeforces/commits{/sha}",
    |    "git_commits_url": "https://api.github.com/repos/njir/codeforces/git/commits{/sha}",
    |    "comments_url": "https://api.github.com/repos/njir/codeforces/comments{/number}",
    |    "issue_comment_url": "https://api.github.com/repos/njir/codeforces/issues/comments{/number}",
    |    "contents_url": "https://api.github.com/repos/njir/codeforces/contents/{+path}",
    |    "compare_url": "https://api.github.com/repos/njir/codeforces/compare/{base}...{head}",
    |    "merges_url": "https://api.github.com/repos/njir/codeforces/merges",
    |    "archive_url": "https://api.github.com/repos/njir/codeforces/{archive_format}{/ref}",
    |    "downloads_url": "https://api.github.com/repos/njir/codeforces/downloads",
    |    "issues_url": "https://api.github.com/repos/njir/codeforces/issues{/number}",
    |    "pulls_url": "https://api.github.com/repos/njir/codeforces/pulls{/number}",
    |    "milestones_url": "https://api.github.com/repos/njir/codeforces/milestones{/number}",
    |    "notifications_url": "https://api.github.com/repos/njir/codeforces/notifications{?since,all,participating}",
    |    "labels_url": "https://api.github.com/repos/njir/codeforces/labels{/name}",
    |    "releases_url": "https://api.github.com/repos/njir/codeforces/releases{/id}",
    |    "created_at": "2015-05-25T06:19:27Z",
    |    "updated_at": "2015-05-28T06:35:26Z",
    |    "pushed_at": "2015-06-05T12:41:32Z",
    |    "git_url": "git://github.com/njir/codeforces.git",
    |    "ssh_url": "git@github.com:njir/codeforces.git",
    |    "clone_url": "https://github.com/njir/codeforces.git",
    |    "svn_url": "https://github.com/njir/codeforces",
    |    "homepage": "",
    |    "size": 360,
    |    "stargazers_count": 0,
    |    "watchers_count": 0,
    |    "language": "C++",
    |    "has_issues": true,
    |    "has_downloads": true,
    |    "has_wiki": true,
    |    "has_pages": false,
    |    "forks_count": 0,
    |    "mirror_url": null,
    |    "open_issues_count": 0,
    |    "forks": 0,
    |    "open_issues": 0,
    |    "watchers": 0,
    |    "default_branch": "master",
    |    "permissions": {
    |      "admin": false,
    |      "push": false,
    |      "pull": true
    |    }
    |  }
    |]
  """.stripMargin

  val userRepoExample =
  """
    |{
    |  "id": 23197208,
    |  "name": "scala",
    |  "full_name": "1ambda/scala",
    |  "owner": {
    |    "login": "1ambda",
    |    "id": 4968473,
    |    "avatar_url": "https://avatars.githubusercontent.com/u/4968473?v=3",
    |    "gravatar_id": "",
    |    "url": "https://api.github.com/users/1ambda",
    |    "html_url": "https://github.com/1ambda",
    |    "followers_url": "https://api.github.com/users/1ambda/followers",
    |    "following_url": "https://api.github.com/users/1ambda/following{/other_user}",
    |    "gists_url": "https://api.github.com/users/1ambda/gists{/gist_id}",
    |    "starred_url": "https://api.github.com/users/1ambda/starred{/owner}{/repo}",
    |    "subscriptions_url": "https://api.github.com/users/1ambda/subscriptions",
    |    "organizations_url": "https://api.github.com/users/1ambda/orgs",
    |    "repos_url": "https://api.github.com/users/1ambda/repos",
    |    "events_url": "https://api.github.com/users/1ambda/events{/privacy}",
    |    "received_events_url": "https://api.github.com/users/1ambda/received_events",
    |    "type": "User",
    |    "site_admin": false
    |  },
    |  "private": false,
    |  "html_url": "https://github.com/1ambda/scala",
    |  "description": "",
    |  "fork": false,
    |  "url": "https://api.github.com/repos/1ambda/scala",
    |  "forks_url": "https://api.github.com/repos/1ambda/scala/forks",
    |  "keys_url": "https://api.github.com/repos/1ambda/scala/keys{/key_id}",
    |  "collaborators_url": "https://api.github.com/repos/1ambda/scala/collaborators{/collaborator}",
    |  "teams_url": "https://api.github.com/repos/1ambda/scala/teams",
    |  "hooks_url": "https://api.github.com/repos/1ambda/scala/hooks",
    |  "issue_events_url": "https://api.github.com/repos/1ambda/scala/issues/events{/number}",
    |  "events_url": "https://api.github.com/repos/1ambda/scala/events",
    |  "assignees_url": "https://api.github.com/repos/1ambda/scala/assignees{/user}",
    |  "branches_url": "https://api.github.com/repos/1ambda/scala/branches{/branch}",
    |  "tags_url": "https://api.github.com/repos/1ambda/scala/tags",
    |  "blobs_url": "https://api.github.com/repos/1ambda/scala/git/blobs{/sha}",
    |  "git_tags_url": "https://api.github.com/repos/1ambda/scala/git/tags{/sha}",
    |  "git_refs_url": "https://api.github.com/repos/1ambda/scala/git/refs{/sha}",
    |  "trees_url": "https://api.github.com/repos/1ambda/scala/git/trees{/sha}",
    |  "statuses_url": "https://api.github.com/repos/1ambda/scala/statuses/{sha}",
    |  "languages_url": "https://api.github.com/repos/1ambda/scala/languages",
    |  "stargazers_url": "https://api.github.com/repos/1ambda/scala/stargazers",
    |  "contributors_url": "https://api.github.com/repos/1ambda/scala/contributors",
    |  "subscribers_url": "https://api.github.com/repos/1ambda/scala/subscribers",
    |  "subscription_url": "https://api.github.com/repos/1ambda/scala/subscription",
    |  "commits_url": "https://api.github.com/repos/1ambda/scala/commits{/sha}",
    |  "git_commits_url": "https://api.github.com/repos/1ambda/scala/git/commits{/sha}",
    |  "comments_url": "https://api.github.com/repos/1ambda/scala/comments{/number}",
    |  "issue_comment_url": "https://api.github.com/repos/1ambda/scala/issues/comments{/number}",
    |  "contents_url": "https://api.github.com/repos/1ambda/scala/contents/{+path}",
    |  "compare_url": "https://api.github.com/repos/1ambda/scala/compare/{base}...{head}",
    |  "merges_url": "https://api.github.com/repos/1ambda/scala/merges",
    |  "archive_url": "https://api.github.com/repos/1ambda/scala/{archive_format}{/ref}",
    |  "downloads_url": "https://api.github.com/repos/1ambda/scala/downloads",
    |  "issues_url": "https://api.github.com/repos/1ambda/scala/issues{/number}",
    |  "pulls_url": "https://api.github.com/repos/1ambda/scala/pulls{/number}",
    |  "milestones_url": "https://api.github.com/repos/1ambda/scala/milestones{/number}",
    |  "notifications_url": "https://api.github.com/repos/1ambda/scala/notifications{?since,all,participating}",
    |  "labels_url": "https://api.github.com/repos/1ambda/scala/labels{/name}",
    |  "releases_url": "https://api.github.com/repos/1ambda/scala/releases{/id}",
    |  "created_at": "2014-08-21T17:54:50Z",
    |  "updated_at": "2015-07-16T20:43:53Z",
    |  "pushed_at": "2015-08-23T14:06:31Z",
    |  "git_url": "git://github.com/1ambda/scala.git",
    |  "ssh_url": "git@github.com:1ambda/scala.git",
    |  "clone_url": "https://github.com/1ambda/scala.git",
    |  "svn_url": "https://github.com/1ambda/scala",
    |  "homepage": null,
    |  "size": 2296,
    |  "stargazers_count": 1,
    |  "watchers_count": 1,
    |  "language": "Scala",
    |  "has_issues": true,
    |  "has_downloads": true,
    |  "has_wiki": true,
    |  "has_pages": false,
    |  "forks_count": 1,
    |  "mirror_url": null,
    |  "open_issues_count": 0,
    |  "forks": 1,
    |  "open_issues": 0,
    |  "watchers": 1,
    |  "default_branch": "master",
    |  "permissions": {
    |    "admin": true,
    |    "push": true,
    |    "pull": true
    |  },
    |  "network_count": 1,
    |  "subscribers_count": 2
    |}
  """.stripMargin
}
