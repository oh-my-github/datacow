package omg.datacow.github

import akka.actor.ActorSystem
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.github.request.{GithubCredential, GetAPIRateLimit}
import omg.datacow.github.response.APIRateLimit
import org.scalatest._

import scala.concurrent.duration._

class GithubControllerSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender 
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  val conf = ConfigFactory.load
  val testCredential = GithubCredential(
    conf.getString("github.id"),
    conf.getString("github.token"))

  def this() = this(ActorSystem("ControllerSpec"))

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "controller should persiste List[Repository] when given a GetUserRepositories message" in {
    val controller = TestActorRef[GithubController]
  }

}
