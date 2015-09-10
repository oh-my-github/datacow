package omg.datacow.github

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.actor.Actor.Receive
import omg.datacow.github.request.{GithubRequest, GithubRequestSenderRouter, GithubRequestSender}
import omg.datacow.github.response.GithubResponse

class GithubController extends Actor with ActorLogging {

  var requestRouter = createRequestRouter
//  val persistenceRouter =

  override val supervisorStrategy = OneForOneStrategy() {
    case _ => Restart
  }

  override def receive: Receive = {
    case message: GithubRequest =>
      log.debug("sad")
      requestRouter ! message
    case message: GithubResponse =>

  }

  def createRequestRouter: ActorRef = context.actorOf(Props[GithubRequestSenderRouter])
//  def createPersistenceRouter: ActorRef = context.actorOf(Props[GithubResponsePersisterRouter])
}
