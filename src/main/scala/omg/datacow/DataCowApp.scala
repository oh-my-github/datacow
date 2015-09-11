package omg.datacow

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import omg.datacow.github.GithubController
import omg.datacow.github.request._

import scala.concurrent.duration._

object DataCowApp extends App {

  val token = sys.env("GITHUB_TOKEN")
  val id = sys.env("GITHUB_ID")
  val credential = GithubCredential(id, token)
  val conf = ConfigFactory.load
  val mongoHost = conf.getString("mongo.production.host")
  val mongoPort = conf.getInt("mongo.production.port")
  val mongoSchema = conf.getString("mongo.production.db")

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(3 seconds)

  val controller = system.actorOf(Props(
    new GithubController(mongoHost, mongoPort, mongoSchema)),
    name="controller")
  controller ! GetAPIRateLimit(credential)
  controller ! GetUserRepositories("1ambda", credential)
  controller ! GetRepositoryLanguages("1ambda", "scala", credential)
}



