package io.github.omg.datacow.github.request

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import com.typesafe.config.ConfigFactory

import spray.client.pipelining._
import spray.http._
import spray.json._

import io.github.omg.datacow.github.response.GithubResponse.Protocol._
import io.github.omg.datacow.github.response.GithubResponse.APIRateLimit

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class GithubRequestSender extends Actor with ActorLogging {
  import context.dispatcher
  implicit val system = context.system
  implicit val timeout = Timeout(15 seconds)

  val logRequest: HttpRequest => HttpRequest = { r => println("before"); r }
  val logResponse: HttpResponse => HttpResponse = { r => println("after"); r }

  def createPipeline(id: String, accessToken: String) = {

    (addHeader("Accept", "application/json")
      ~> addCredentials(BasicHttpCredentials(id, accessToken))
      ~> sendReceive
      ~> unmarshal[String])
  }

  override def receive = {
    case GetRateLimit(id, accessToken) =>
      println(s"GetRateLimit($id, $accessToken)")

      val pipeline = createPipeline(id, accessToken)
      val res = pipeline(Get(Uri("https://api.github.com/rate_limit")))

      res.onComplete {
        case Success(response) =>
          val rateLimit = response.parseJson.convertTo[APIRateLimit]
          log.info(rateLimit.toString)
          system terminate

        case Failure(t) =>
          println(t.getMessage)
          system terminate
      }
  }
}
