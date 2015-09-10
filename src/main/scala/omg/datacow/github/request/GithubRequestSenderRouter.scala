package omg.datacow.github.request

import akka.actor.Actor.Receive
import akka.actor._
import akka.routing._

class GithubRequestSenderRouter extends Actor with ActorLogging {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(routeePropFactory)
      context watch r
      ActorRefRoutee(r)
    }

    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Actor.Receive = {
    case Terminated(child) =>
      router = router.removeRoutee(child)
      val r = context.actorOf(routeePropFactory)
      context watch r
      router = router.addRoutee(r)
    case message: GithubRequest =>
      router.route(message, sender())
  }

  def routeePropFactory = Props[GithubRequestSender]
}
