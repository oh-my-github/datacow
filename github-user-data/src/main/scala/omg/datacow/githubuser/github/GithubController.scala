package omg.datacow.githubuser.github

import omg.datacow.githubuser.user.UserStatisticsUpdateActor.RetrieveUserAccessToken

import scala.concurrent.duration._
import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import omg.datacow.githubuser.github.request._
import omg.datacow.githubuser.github.response.GithubResponsePersistActor.{PersisterEvent, Failed, Persisted}
import omg.datacow.githubuser.github.response._
import omg.datacow.githubuser.user.UserStatisticsUpdateActor

class GithubController extends Actor with ActorLogging {

  import context.dispatcher

  var requestRouter     = createRequestRouter
  val persistenceRouter = createPersistenceRouter
  var updateScheduler = createUserStatUpdateScheduler
  var origin: Option[ActorRef] = None

  context.system.scheduler.schedule(
    5 seconds, 30 seconds,
    updateScheduler, RetrieveUserAccessToken)

  override val supervisorStrategy = OneForOneStrategy() {
    case _ => Restart
  }

  override def receive: Receive = {
    case message: GithubRequest =>
      log.info(message.toString)
      requestRouter ! message

    case message: GithubResponse =>
      log.info(message.toString)
      // TODO origin check using Map
      origin = Some(sender)
      persistenceRouter ! message

    case message: PersisterEvent =>
      origin foreach { _ ! message }
      origin = None

    case message: Any =>
      log.warning("unhandled message: " + message)
  }

  def createRequestRouter: ActorRef =
    context.actorOf(Props[GithubRequestSendRouter])

  def createPersistenceRouter: ActorRef =
    context.actorOf(Props[GithubResponsePersistRouter])

  def createUserStatUpdateScheduler =
    context.actorOf(Props(
      new UserStatisticsUpdateActor(self)))
}
