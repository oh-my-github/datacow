package omg.datacow.user

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.typesafe.config.ConfigFactory
import omg.datacow.util.{UserProfileFixture, MongoUtil}
import org.scalatest._

class UserProfileSpec extends FunSuite with Matchers with BeforeAndAfterEach {
  import UserProfileFixture._

  val conf = ConfigFactory.load
  lazy val conn = MongoUtil.getTestMongoConn
  lazy val users = conn(UserProfile.userCollectionName)

  override def beforeEach = {
    MongoUtil.initialize
  }

  override def afterEach = { MongoUtil.stop }

  test("persisted dbo user can be converted back to scala User") {
    val dbo = grater[UserProfile].asDBObject(user1)
    users.insert(dbo)
    val user = users.findOne(MongoDBObject("id" -> user1.id)).get
    val scalao = grater[UserProfile].asObject(user)

    scalao shouldBe user1
  }
}


