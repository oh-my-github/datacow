package omg.datacow.user

import com.typesafe.config.ConfigFactory
import omg.datacow.util.MongoUtil
import org.scalatest._

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

class UserStatisticsUpdaterSpec extends FunSuite with Matchers with BeforeAndAfterEach {
  import UserFixtures._

  val conf = ConfigFactory.load
  lazy val conn = MongoUtil.getTestEnvMongoSchema
  lazy val users = conn("users")

  override def beforeEach = { MongoUtil.initialize }
  override def afterEach = { MongoUtil.stop }

  test("update user profile if exists") {
    // user1 and user2 have the same UserProfile.id
    val user1Dbo = grater[UserProfile].asDBObject(user1)
    users.insert(user1Dbo)

    val foundDbo = users.findOne(MongoDBObject("id" -> user1.id)).get
    val foundUser1 = grater[UserProfile].asObject(foundDbo)

    // exists
    foundUser1 shouldBe user1



  }

  test("insert new user profile if not exists") {

  }
}

