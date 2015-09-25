package omg.datacow.github

import omg.datacow.user.UserStatisticsUpdateScheduler.RetrieveUserAccessToken

import scala.concurrent.duration._
import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import omg.datacow.github.request._
import omg.datacow.github.response.GithubResponsePersister.{PersisterEvent, Failed, Persisted}
import omg.datacow.github.response._
import omg.datacow.persistent.MongoConfig
import omg.datacow.user.UserStatisticsUpdateScheduler

class GithubController(mongoConfig: MongoConfig) extends Actor with ActorLogging {

  import context.dispatcher

  var requestRouter     = createRequestRouter
  val persistenceRouter = createPersistenceRouter
  var updateScheduler = createUserStatUpdateScheduler
  var origin: Option[ActorRef] = None

  context.system.scheduler.schedule(
    60 seconds, 600 seconds,
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
      origin = Some(sender)
      persistenceRouter ! message

    case message: PersisterEvent =>
      origin foreach { _ ! message }
      origin = None

    case message: Any =>
      log.warning("unhandled message: " + message)
  }

  def createRequestRouter: ActorRef =
    context.actorOf(Props[GithubRequestSenderRouter])

  def createPersistenceRouter: ActorRef =
    context.actorOf(Props(
      new GithubResponsePersisterRouter(mongoConfig)))

  def createUserStatUpdateScheduler =
    context.actorOf(Props(
      new UserStatisticsUpdateScheduler(self, mongoConfig)))
}
