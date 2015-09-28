package omg.datacow.util

import com.github.nscala_time.time.Imports.DateTime
import omg.datacow.github.response._
import omg.datacow.user.{GithubProfile, UserProfile}

import scala.concurrent.duration._

import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Fixtures {
  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

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

  val langs1 = Languages(
    DateTime.now,
    "1ambda", "scala",
    List(
      Language("scala", 30114),
      Language("haskell", 20104),
      Language("lisp", 3014)
    ))

  val langs2 = Languages(
    DateTime.now,
    "1ambda", "scala",
    List(
      Language("scala", 30114),
      Language("haskell", 20104),
      Language("lisp", 3014)
    ))

  val repo1 = Repository(
    DateTime.now,
    "1ambda", "scala", "1ambda/scala", false, false,
    new DateTime("2015-09-08"), new DateTime("2015-09-25"), new DateTime("2015-09-25"),
    10L, 5L, 3L)

  val repo2 = Repository(
    DateTime.now,
    "1ambda", "haskell", "1ambda/haskell", false, false,
    new DateTime("2015-07-08"), new DateTime("2015-09-22"), new DateTime("2015-09-22"),
    0L, 1L, 2L)

  val repo3 = Repository(
    DateTime.now,
    "njir", "node", "njir/node", false, false,
    new DateTime("2015-09-08"), new DateTime("2015-09-08"), new DateTime("2015-09-09"),
    5L, 10L, 3L)
}
