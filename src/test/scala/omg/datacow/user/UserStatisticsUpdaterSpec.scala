package omg.datacow.user

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.github.response.{Repository, Repositories}
import omg.datacow.persistent.MongoConfig
import omg.datacow.util.{UserProfileFixture, MongoUtil}
import org.scalatest._

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

class UserStatisticsUpdaterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterEach {

  import UserProfileFixture._
  import omg.datacow.util.UserStatFixture._
  import UserStatisticsUpdater._

  def this() = this(ActorSystem("UserStatisticsUpdaterSystem"))

  val conf = ConfigFactory.load
  lazy val conn = MongoUtil.getTestEnvMongoSchema
  lazy val users = conn("users")
  val mongoConfig = MongoUtil.getTestMongoConfig

  override def beforeEach = { MongoUtil.initialize }
  override def afterEach = { MongoUtil.stop }

  "update user repo if outdated" in {
    // user1 and user2 have the same UserProfile.id
    val user1Dbo = grater[UserProfile].asDBObject(user1)
    users.insert(user1Dbo)

    val lang1Dbo = grater[Repository].asDBObject(repo1)

    val foundDbo = users.findOne(MongoDBObject("id" -> user1.id)).get
    val foundUser1 = grater[UserProfile].asObject(foundDbo)

    // exists
    foundUser1 shouldBe user1
  }

  "insert user repo if not exists" in {
    val controller = TestProbe()
    val updater = createUpdater(controller.ref, mongoConfig)
    updater ! RetrieveUserAccessToken
  }

  "do nothing if user repo exists and already it was updated" in {

  }

  def createUpdater(controller: ActorRef, config: MongoConfig): ActorRef = {
    system.actorOf(Props(new UserStatisticsUpdater(controller, config)))
  }

}


