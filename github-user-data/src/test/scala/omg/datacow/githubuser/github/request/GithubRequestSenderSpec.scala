package omg.datacow.githubuser.github.request

import akka.actor.ActorSystem
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.githubuser.github.response._
import omg.datacow.githubuser.util.TestUtility
import org.scalatest._
import scala.concurrent.duration._
import com.github.nscala_time.time.Imports.DateTime

class GithubRequestSenderSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  def this() = this(ActorSystem("GithubRequestSender"))

  val conf = ConfigFactory.load
  val testCredential = TestUtility.getTestCredential

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  // TODO non-breaking validation using scalaz NPE
  "sender should handle an invalid credential failure " in {
    val sender = TestActorRef[GithubRequestSendActor]
    val invalidCredential = GithubCredential("invalidId", "invalidSecret")
    sender ! GetUserRepositories("1ambda", invalidCredential, DateTime.now)
    expectMsgPF(10 seconds) {
      case GithubRequestSendActor.RequestFailed => ()
    }
  }

  "sender should return ApiRateLimit case class when given a GetAPIRateLimit" in {
    val requestSender = TestActorRef[GithubRequestSendActor]
    requestSender ! GetAPIRateLimit(testCredential, DateTime.now)
    expectMsgPF(10 seconds) {
      case APIRateLimit(_, _) => ()
    }
  }

  "sender should return a Repository list when given a GetUserRepositories" in {
    val requestSender = TestActorRef[GithubRequestSendActor]
    requestSender ! GetUserRepositories("1ambda", testCredential, DateTime.now)
    expectMsgPF(10 seconds) {
      case Repositories(_) => ()
    }
  }

  "sender should return Languages case class when given a GetRepositoryLanguages" in {
    val requestSender = TestActorRef[GithubRequestSendActor]
    requestSender ! GetRepositoryLanguages("1ambda", testCredential, DateTime.now, "scala")
    expectMsgPF(10 seconds) {
      case Languages(_, _, _, _) => ()
    }
  }
}
