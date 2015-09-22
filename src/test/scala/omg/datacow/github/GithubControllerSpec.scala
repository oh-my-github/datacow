package omg.datacow.github

import akka.actor.{Props, ActorSystem}
import akka.testkit._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoCollection, MongoClient}
import com.typesafe.config.ConfigFactory
import omg.datacow.github.request._
import omg.datacow.github.response._
import omg.datacow.persistent.MongoConfig
import omg.datacow.util.MongoUtil
import org.scalatest._
import omg.datacow.github.response.GithubResponsePersister._

import scala.concurrent.duration._

class GithubControllerSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  val conf = ConfigFactory.load
  lazy val conn = MongoUtil.getTestEnvMongoSchema
  lazy val languages: MongoCollection = conn(languageCollectionName)
  lazy val repositories: MongoCollection =  conn(repositoryCollectionName)

  def this() = this(ActorSystem("ControllerSpec"))

  override def beforeAll() = {
    MongoUtil.initialize
  }

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
    MongoUtil.stop
  }

  "controller should return Persisted message when given a Languages case class" in {
    val controller = createTestController
    val langs = Languages(
      "2015-09-07T22:50:08.699+09:00", "1ambda", "scala",
      List(
        Language("scala", 30114),
        Language("haskell", 20104),
        Language("lisp", 3014)
      ))

    controller ! langs
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  "controller should return Persisted message when given a Repositories case class" in {
    val controller = createTestController

    val repo1 = Repository(
      "2015-09-07T22:50:08.699+09:00",
      "1ambda", "scala", "1ambda/scala", false, false,
      "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

    val repo2 = Repository(
      "2015-09-07T22:50:08.699+09:00",
      "1ambda", "scala", "1ambda/haskell", false, false,
      "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

    controller ! Repositories(List(repo1, repo2))
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }
  
  def createTestController =  {
    val mongoConfig = MongoUtil.getTestMongoConfig
    TestActorRef(Props(new GithubController(mongoConfig)))
  }
}
