package omg.datacow.github.response

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.github.response.GithubResponse._
import omg.datacow.github.response.GithubResponsePersister._
import omg.datacow.util.MongoUtil
import org.scalatest._

import scala.concurrent.duration._

class GithubResponsePersisterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  def this() = this(ActorSystem("GithubResponseProcessorSpec"))

  override def beforeAll: Unit = {
    MongoUtil.initialize
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
    MongoUtil.stop
  }

  "should persist Repository to the repository collection" in {
    val persister = createPersister

    val repo1 = Repository(
      "2015-09-07T22:50:08.699+09:00",
      "1ambda", "scala", "1ambda/scala", false, false,
      "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

    val repo2 = Repository(
      "2015-09-07T22:50:08.699+09:00",
      "1ambda", "scala", "1ambda/haskell", false, false,
      "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

    persister ! Repositories(List(repo1, repo2))
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  "should persist Languages to the language collection" in {
    val persister = createPersister

    val langs = Languages(
      "2015-09-07T22:50:08.699+09:00", "1ambda", "scala",
      List(
        Language("scala", 30114),
        Language("haskell", 20104),
        Language("lisp", 3014)
      ))

    persister ! langs
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  "should return Failed when given an undefined GithubResponse" in {
    val persister = createPersister

    val emptyRate = Rate(1, 1, 1L)
    persister ! APIRateLimit(Resources(emptyRate, emptyRate), emptyRate)

    expectMsgPF(10 seconds) {
      case GithubResponsePersister.Failed => ()
    }
  }

  def createPersister = {
    val conf = ConfigFactory.load
    val mongoHost = conf.getString("mongo.test.host")
    val mongoPort = conf.getInt("mongo.test.port")
    val mongoSchema = conf.getString("mongo.test.db")

    TestActorRef(Props(new GithubResponsePersister(mongoHost, mongoPort, mongoSchema)))
  }
}

