package io.github.omg.datacow.github.request

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import com.typesafe.config.ConfigFactory
import io.github.omg.datacow.github.response.GithubResponse

import spray.client.pipelining._
import spray.http._
import spray.json._

import io.github.omg.datacow.github.response.GithubResponse.Protocol._
import io.github.omg.datacow.github.response.GithubResponse.APIRateLimit

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class GithubRequestSender extends Actor with ActorLogging {
  import GithubResponse._

  import context.dispatcher
  implicit val system = context.system
  implicit val timeout = Timeout(15 seconds)

  val logRequest: HttpRequest => HttpRequest = { r => println("before"); r }
  val logResponse: HttpResponse => HttpResponse = { r => println("after"); r }

  def createPipeline(credential: GithubCredential) = {

    (addHeader("Accept", "application/json")
      ~> addCredentials(BasicHttpCredentials(credential.id, credential.accessToken))
      ~> sendReceive
      ~> unmarshal[String])
  }

  override def receive = {
    case req@GetAPIRateLimit(credential) =>

      val pipeline = createPipeline(credential)
      val res = pipeline(Get(Uri(req.url)))

      res.onComplete {
        case Success(response) =>
          val rateLimit = response.parseJson.convertTo[APIRateLimit]
          log.info(rateLimit.toString)

        case Failure(t) =>
          println(t.getMessage)
      }


    case req@GetRepositories(owner, credential) =>
      createPipeline(credential)(Get(Uri(req.url))) onComplete {
        case Success(response) =>
          val repos = response.parseJson.convertTo[List[Repository]]
          log.info(repos.toString)

        case Failure(t) =>
          println(t.getMessage)
      }


    case req@GetRepositoryLanguages(owner, repository, credential) =>
      createPipeline(credential)(Get(Uri(req.url))) onComplete {
        case Success(response) =>
          val repos = response.parseJson.convertTo[List[Language]]
          val langs = Languages(owner, repository, repos)
          log.info(langs.toString)

        case Failure(t) =>
          println(t.getMessage)
      }
  }
}
