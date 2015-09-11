package omg.datacow

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import omg.datacow.github.GithubController
import omg.datacow.github.request._

import scala.concurrent.duration._

object DataCowApp extends App {

  val conf = ConfigFactory.load
  val token = conf.getString("github.token")
  val id = conf.getString("github.id")

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(3 seconds)

  val controller = system.actorOf(Props[GithubController], name="controller")

  val credential = GithubCredential(id, token)

  controller ! GetAPIRateLimit(credential)
  controller ! GetUserRepositories("1ambda", credential)
//  controller ! GetRepositoryLanguages("1ambda", "scala", credential)
}



