package omg.datacow.user

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.typesafe.config.ConfigFactory
import omg.datacow.DataCowConfig
import omg.datacow.persistent.{UserProfileDAO, MongoUtils}
import omg.datacow.util.{UserProfileFixture, TestEnvMongoUtil}
import org.scalatest._

class UserProfileSpec extends FunSuite with Matchers with BeforeAndAfterEach {
  import UserProfileFixture._

  lazy val users = MongoUtils.getUserCollection

  override def beforeEach = {
    TestEnvMongoUtil.initialize
  }

  override def afterEach = { TestEnvMongoUtil.stop }

  test("persisted dbo user can be converted back to scala User") {
    UserProfileDAO.insert(user1)
    val found =  UserProfileDAO.findOneById(id = user1._id).get

    found shouldBe user1
  }
}


