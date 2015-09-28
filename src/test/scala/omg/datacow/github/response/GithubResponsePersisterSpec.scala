package omg.datacow.github.response

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.github.response.GithubResponse._
import omg.datacow.github.response.GithubResponsePersister._
import omg.datacow.util.TestEnvMongoUtil
import org.scalatest._

import scala.concurrent.duration._

class GithubResponsePersisterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  import omg.datacow.util.Fixtures._

  def this() = this(ActorSystem("GithubResponsePersisterSpec"))

  override def beforeEach = { TestEnvMongoUtil.dropDatabase }
  override def afterAll = { TestKit.shutdownActorSystem(system) }

  "should persist Repository to the repository collection" in {
    val persister = createPersister

    persister ! Repositories(List(repo1, repo3))
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  "should persist Languages to the language collection" in {
    val persister = createPersister

    persister ! langs1
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

  "PersistenceRouter should persiste GithubResponse.Languages messages" in {
    val router = createPersistenceRouter

    router ! langs1
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  def createPersistenceRouter = {
    TestActorRef[GithubResponsePersisterRouter]
  }

  def createPersister = {
    TestActorRef(Props[GithubResponsePersister])
  }
}

