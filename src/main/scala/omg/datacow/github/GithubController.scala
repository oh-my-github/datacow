package omg.datacow.github

import akka.actor.Actor
import akka.actor.Actor.Receive

class GithubController extends Actor {
  override def receive: Receive = {
    case message => sender ! message
  }
}
