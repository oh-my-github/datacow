package omg.datacow.util

import omg.datacow.user._
import com.github.nscala_time.time.Imports.DateTime
import org.bson.types.ObjectId

object UserProfileFixture {

  val user1 = UserProfile(
    new ObjectId,
    "1ambda",
    GithubProfile(
      "4910051",
      "1ambda",
      "1ambda_at_gmail.com",
      sys.env("GITHUB_ACCESS_TOKEN"),
      "",
      49L,
      29L,
      new DateTime("2013-07-08T22:03:28Z"),
      new DateTime("2015-09-09T22:03:28Z"),
      None,
      None,
      Some("asdasd")
    )
  )

  val user2 = UserProfile(
    new ObjectId,
    "njir",
    GithubProfile(
      "4910052",
      "jnir",
      "njir_at_gmail.com",
      sys.env("GITHUB_ACCESS_TOKEN"),
      "",
      23L,
      89L,
      new DateTime("2013-07-08T22:03:28Z"),
      new DateTime("2015-09-22T22:03:28Z"),
      None,
      None,
      Some("asdasd")
    )
  )

  val user3 = UserProfile(
    new ObjectId,
    "tocology",
    GithubProfile(
      "4910053",
      "tocology",
      "tocology_at_gmail.com",
      sys.env("GITHUB_ACCESS_TOKEN"),
      "",
      23L,
      89L,
      new DateTime("2013-08-08T22:03:28Z"),
      new DateTime("2015-09-21T22:03:28Z"),
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
    | "githubProfile": {
    |   "id" "4959181" /* github.id"
    |   "login": "1ambda", /* github.login */
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
