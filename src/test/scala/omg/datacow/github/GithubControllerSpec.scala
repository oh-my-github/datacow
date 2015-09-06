package omg.datacow.github

import akka.actor.ActorSystem
import akka.testkit._
import omg.datacow.github.request.GetAPIRateLimit
import org.scalatest._

class GithubControllerSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender 
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {
  
  def this() = this(ActorSystem("ControllerSpec"))

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  // 1. GetApiRateLimit 를 받으면 GithubRequestSender 로 전달해야 함
  // 2. ApiRateLimit 메세지를 Sender 로 부터 받으면 GithubResponseProcessor 로 전달해야함
  "controller should pass GithubRequest Message to GithubRequestSende" in {
    val controller = TestActorRef[GithubController]
  }

}
