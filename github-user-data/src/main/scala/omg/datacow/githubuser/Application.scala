package omg.datacow.githubuser

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import omg.datacow.githubuser.github.GithubController
import scala.concurrent.duration._

object Application extends App {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(3 seconds)

  val controller = system.actorOf(Props[GithubController])
}
