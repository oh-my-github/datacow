package omg.datacow.github.response

import omg.datacow.github.response.GithubResponsePersistActor.Persisted
import omg.datacow.util.TestEnvMongoUtil

import scala.concurrent.duration._

import akka.actor._
import akka.testkit._

import com.typesafe.config.ConfigFactory

import org.scalatest._

class GithubResponsePersisterRouterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  import omg.datacow.util.Fixtures._

  def this() = this(ActorSystem("GithubResponsePersisterRouterSpec"))
  val conf = ConfigFactory.load

  override def beforeEach = { TestEnvMongoUtil.dropDatabase }
  override def afterAll = { TestKit.shutdownActorSystem(system) }

}
