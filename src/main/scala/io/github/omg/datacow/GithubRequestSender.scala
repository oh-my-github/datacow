package io.github.omg.datacow

import akka.actor.{ActorLogging, Actor}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import spray.http._
import spray.client.pipelining._

import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.duration._
import scala.util.{Success, Failure}

class GithubRequestSender extends Actor with ActorLogging {
  import context.dispatcher
  implicit val system = context.system
  implicit val timeout = Timeout(15 seconds)

  val logRequest: HttpRequest => HttpRequest = { r => println("before"); r }
  val logResponse: HttpResponse => HttpResponse = { r => println("after"); r }

  val conf = ConfigFactory.load()
  val token = conf.getString("github.token")
  val id = conf.getString("github.id")

  override def receive = {
    case "request" =>

      val pipeline = (
        addHeader("Accept", "application/json")
          ~> addCredentials(BasicHttpCredentials("1ambda", token))
          ~> sendReceive
          ~> unmarshal[String]
        )

      val res = pipeline(Get(Uri("https://api.github.com/rate_limit")))

      res.onComplete {
        case Success(response) =>
          log.info(response.parseJson.prettyPrint)
          system terminate

        case Failure(t) =>
          println(t.getMessage)
          system terminate
      }
  }
}
