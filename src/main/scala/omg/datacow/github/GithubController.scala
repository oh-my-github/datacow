package omg.datacow.github

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.actor.Actor.Receive
import omg.datacow.github.request._
import omg.datacow.github.response._

class GithubController extends Actor with ActorLogging {

  var requestRouter     = createRequestRouter
  val persistenceRouter = createPersistenceRouter

  override val supervisorStrategy = OneForOneStrategy() {
    case _ => Restart
  }

  override def receive: Receive = {
    case message: GithubRequest =>
      requestRouter ! message
    case message: GithubResponse =>
      persistenceRouter ! message
  }

  def createRequestRouter: ActorRef = context.actorOf(Props[GithubRequestSenderRouter])
  def createPersistenceRouter: ActorRef = context.actorOf(Props[GithubResponsePersisterRouter])
}
