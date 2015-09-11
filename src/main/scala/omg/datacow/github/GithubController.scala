package omg.datacow.github

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.actor.Actor.Receive
import com.typesafe.config.ConfigFactory
import omg.datacow.github.request._
import omg.datacow.github.response.GithubResponsePersister.{PersisterEvent, Failed, Persisted}
import omg.datacow.github.response._

class GithubController extends Actor with ActorLogging {

  val conf = ConfigFactory.load
  val mongoHost = conf.getString("mongo.production.host")
  val mongoPort = conf.getInt("mongo.production.port")
  val mongoSchema = conf.getString("mongo.production.db")

  var requestRouter     = createRequestRouter
  val persistenceRouter = createPersistenceRouter
  var origin: Option[ActorRef] = None

  override val supervisorStrategy = OneForOneStrategy() {
    case _ => Restart
  }

  override def receive: Receive = {
    case message: GithubRequest =>
      requestRouter ! message

    case message: GithubResponse =>
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
    context.actorOf(Props(new GithubResponsePersisterRouter(mongoHost, mongoPort, mongoSchema)))
}
