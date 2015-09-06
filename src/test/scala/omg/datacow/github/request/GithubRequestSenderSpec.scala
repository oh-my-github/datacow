package omg.datacow.github.request

import akka.actor.ActorSystem
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.github.response._
import org.scalatest._
import scala.concurrent.duration._

class GithubRequestSenderSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  def this() = this(ActorSystem("GithubRequestSender"))

  val conf = ConfigFactory.load
  val testCredential = GithubCredential(
    conf.getString("github.id"),
    conf.getString("github.token"))

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "controller should return ApiRateLimit case class when given a GetAPIRateLimit" in {
    val requestSender = TestActorRef[GithubRequestSender]
    requestSender ! GetAPIRateLimit(testCredential)
    expectMsgPF(10 seconds) {
      case APIRateLimit(_, _) => ()
    }
  }

  "controller should return a Repository list when given a GetUserRepositories" in {
    val requestSender = TestActorRef[GithubRequestSender]
    requestSender ! GetUserRepositories("1ambda", testCredential)
    expectMsgType[List[Repository]](10 seconds)
  }

  "controller should return Languages case class when given a GetRepositoryLanguages" in {
    val requestSender = TestActorRef[GithubRequestSender]
    requestSender ! GetRepositoryLanguages("1ambda", "scala", testCredential)
    expectMsgType[Languages](10 seconds)
  }
}
