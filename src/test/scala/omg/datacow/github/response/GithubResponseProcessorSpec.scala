package omg.datacow.github.response

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest._

class GithubResponseProcessorSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("GithubResponseProcessorSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "processor should persist message" in {
    val conf = ConfigFactory.load
    val mongoHost = conf.getString("mongo.test.host")
    val mongoPort = conf.getInt("mongo.test.port")

    val processor = TestActorRef(Props(new GithubResponseProcessor(mongoHost, mongoPort)), name = "processor")
    val message = "hello processor"
    processor ! message
    expectMsg(message)
  }
}
