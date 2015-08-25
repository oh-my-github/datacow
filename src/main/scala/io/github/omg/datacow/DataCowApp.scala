package io.github.omg.datacow

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.io.IO
import akka.pattern._

import com.typesafe.config.{Config, ConfigFactory}

import spray.can.Http
import spray.http._
import spray.client.pipelining._
import HttpMethods._

import scala.util.parsing.json.JSONObject
import scala.util.{Failure, Success}
import scalaz.{Free, ~>, Id, Coyoneda}

object DataCowApp extends App {
  import GithubService._

  val id = "1ambda"

  val repos = run(getUserRepositoryNames(id))
  val statuses = run(getRepositoryStatus(id))
  val conf = ConfigFactory.load()
  val token = conf.getString("github.token")

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(15 seconds)
  import system.dispatcher

  val logRequest: HttpRequest => HttpRequest = { r => println(r); r }
  val logResponse: HttpResponse => HttpResponse = { r => println(r); r }

  val pipeline = (
    addHeader("Content-Type", "application/json")
      ~> addCredentials(BasicHttpCredentials("1ambda", "token"))
      ~> sendReceive
      ~> logResponse
    )

  println(token)

  val res: Future[HttpResponse] = (IO(Http) ?
    HttpRequest(GET, Uri("https://api.github.com/rate_limit")))
    .mapTo[HttpResponse]

  res.onComplete {
    case Success(response) => println(response); system.terminate
    case Failure(t) => println(t.getMessage)
  }
}



