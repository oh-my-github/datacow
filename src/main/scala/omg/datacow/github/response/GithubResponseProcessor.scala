package omg.datacow.github.response

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

class GithubResponseProcessor(host : String, port: Int) extends Actor {
  override def receive: Receive = {
    case msg => sender ! msg
  }
}
