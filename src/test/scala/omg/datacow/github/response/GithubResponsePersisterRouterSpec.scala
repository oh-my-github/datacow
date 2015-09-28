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

  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  import omg.datacow.util.UserStatFixture._
  import MongoUtil._

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

    router ! langs1
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  def createPersistenceRouter = {
    TestActorRef[GithubResponsePersisterRouter]
  }
}
