package omg.datacow

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import omg.datacow.github.request._

import scala.concurrent.duration._

object DataCowApp extends App {

  val conf = ConfigFactory.load()
  val token = conf.getString("github.token")
  val id = conf.getString("github.id")

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(3 seconds)

  val sender = system.actorOf(Props[GithubRequestSender], name="sender")

  val credential = GithubCredential(id, token)

  sender ! GetAPIRateLimit(credential)
  sender ! GetRepositories("1ambda", credential)
  sender ! GetRepositoryLanguages("1ambda", "scala", credential)
}



