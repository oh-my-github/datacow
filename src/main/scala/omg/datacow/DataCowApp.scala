package omg.datacow

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import omg.datacow.github.GithubController

import scala.concurrent.duration._

object DataCowApp extends App {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(3 seconds)

  val controller = system.actorOf(Props[GithubController])
}



