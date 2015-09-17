package omg.datacow.user

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.typesafe.config.ConfigFactory
import omg.datacow.util.MongoUtil
import org.scalatest._

class UserSpec extends FunSuite with Matchers with BeforeAndAfterEach {
  import UserSpec._

  val conf = ConfigFactory.load
  lazy val conn = MongoUtil.getTestEnvMongoSchema
  lazy val users = conn(User.userCollectionName)

  override def beforeEach = {
    MongoUtil.initialize
  }

  override def afterEach = { MongoUtil.stop }

  test("journaled user can be converted back to scala User") {
    val dbo = grater[User].asDBObject(user1)
    users.insert(dbo)
    val user = users.findOne(MongoDBObject("id" -> user1.id)).get
    val scalao = grater[User].asObject(user)

    scalao shouldBe user1
  }
}

object UserSpec {

  val user1 = User(
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

  val user2 = User(
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
