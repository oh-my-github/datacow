package omg.datacow.github.response

import omg.datacow.github.response.GithubResponsePersister.Persisted
import omg.datacow.util.MongoUtil

import scala.concurrent.duration._

import akka.actor._
import akka.testkit._

import com.typesafe.config.ConfigFactory

import org.scalatest._

class GithubResponsePersisterRouterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  def this() = this(ActorSystem("GithubResponsePersisterRouterSpec"))
  val conf = ConfigFactory.load

  override def beforeAll() = {
    MongoUtil.initialize
  }

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
    MongoUtil.stop
  }

  "PersistenceRouter should persiste GithubResponse.Languages messages" in {
    val router = createPersistenceRouter

    val langs = Languages(
      "2015-09-07T22:50:08.699+09:00", "1ambda", "scala",
      List(
        Language("scala", 30114),
        Language("haskell", 20104),
        Language("lisp", 3014)
      ))

    router ! langs
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  def createPersistenceRouter = {
    val mongoHost = conf.getString("mongo.test.host")
    val mongoPort = conf.getInt("mongo.test.port")
    val mongoSchema = conf.getString("mongo.test.db")
    TestActorRef(Props(new GithubResponsePersisterRouter(mongoHost, mongoPort, mongoSchema)))
  }
}
