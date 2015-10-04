package omg.datacow.github

import akka.actor._
import akka.testkit._
import org.scalatest._

import omg.datacow.github.response._
import omg.datacow.util.TestEnvMongoUtil
import omg.datacow.github.response.GithubResponsePersistActor._
import omg.datacow.persistent.MongoUtils

import scala.concurrent.duration._
import com.github.nscala_time.time.Imports.DateTime
import com.mongodb.casbah._

class GithubControllerSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  def this() = this(ActorSystem("ControllerSpec"))

  override def beforeEach = { TestEnvMongoUtil.dropDatabase }
  override def afterAll = { TestKit.shutdownActorSystem(system) }

  "controller should return Persisted message when given a Languages case class" in {
    val controller = createTestController
    val langs = Languages(
      DateTime.now, "1ambda", "scala",
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
      DateTime.now,
      "1ambda", "scala", "1ambda/scala", false, false,
      new DateTime("2015-09-08"), new DateTime("2015-09-08"), new DateTime("2015-09-09"),
      10L, 1L, 2L)

    val repo2 = Repository(
      DateTime.now,
      "1ambda", "scala", "1ambda/haskell", false, false,
      new DateTime("2015-09-08"), new DateTime("2015-09-08"), new DateTime("2015-09-09"),
      10L, 1L, 2L)

    controller ! Repositories(List(repo1, repo2))
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }
  
  def createTestController =  {
    TestActorRef(Props[GithubController])
  }
}
