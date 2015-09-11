package omg.datacow.github.response

import akka.actor._
import akka.routing._

class GithubResponsePersisterRouter(host: String, port: Int, schema: String)
  extends Actor with ActorLogging {

   var router = {
     val routees = Vector.fill(5) {
       val r = context.actorOf(Props(new GithubResponsePersister(host, port, schema)))
       context watch r
       ActorRefRoutee(r)
     }

     Router(RoundRobinRoutingLogic(), routees)
   }

   override def receive: Actor.Receive = {
     case Terminated(child) =>
       router = router.removeRoutee(child)
       val r = context.actorOf(Props(new GithubResponsePersister(host, port, schema)))
       context watch r
       router = router.addRoutee(r)
     case message: GithubResponse =>
       router.route(message, sender())
   }
 }
