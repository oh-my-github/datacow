package omg.datacow.util

import omg.datacow.user.{GithubProfile, UserProfile}

object UserProfileFixture {

  val user1 = UserProfile(
    "104919591",
    "1ambda",
    GithubProfile(
      "4910051",
      "1ambda",
      "1ambda_at_gmail.com",
      sys.env("GITHUB_ACCESS_TOKEN"),
      "",
      49L,
      29L,
      "2013-07-08T22:03:28Z",
      "2015-09-09T22:03:28Z",
      None,
      None,
      Some("asdasd")
    )
  )

  val user2 = UserProfile(
    "104919591",
    "1ambda",
    GithubProfile(
      "4910051",
      "1ambda",
      "1ambda_at_gmail.com",
      sys.env("GITHUB_ACCESS_TOKEN"),
      "",
      50L,
      29L,
      "2013-07-08T22:03:28Z",
      "2015-09-10T22:03:28Z",
      None,
      None,
      Some("asdasd")
    )
  )

  val exampleUserString =
  s"""
    |{
    | "id": "104019591",
    | "name": "1ambda", /* github.login */
    | "github": {
    |   "id" "4959181" /* github.id"
    |   "name": "1ambda", /* github.login */
    |   "email": "1amb4a@gmail",
    |   "accessToken": ${sys.env("GITHUB_ACCESS_TOKEN")},
    |   "refreshToken": "0041lalfpa",
    |   "following": 401,
    |   "followers": 299,
    |   "createdAt": "2013-07-08T22:03:28Z", /* ISO date */
    |   "updatedAt": "2015-09-08T12:03:28Z", /* ISO date */
    |   "blog": "",
    |   "company": "",
    |   "location": "Seoul"
    | }
  """.stripMargin

}
