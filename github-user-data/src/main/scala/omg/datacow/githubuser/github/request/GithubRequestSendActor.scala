package omg.datacow.githubuser.github.request

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import omg.datacow.githubuser.github.response.GithubResponse._

import spray.client.pipelining._
import spray.http._
import spray.json._
import omg.datacow.githubuser.github.response._
import omg.datacow.githubuser.github.response.Language

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class GithubRequestSendActor extends Actor with ActorLogging {
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
    case req: GithubRequest =>

      val pipeline = createPipeline(req.credential)
      val res = pipeline(Get(Uri(req.uri)))
      val controller = sender

      res.onComplete {
        case Success(response) =>
          parseGithubResponse(req, response) match {
            case Success(parsed) =>
              controller ! parsed
            case Failure(t) =>
              log.error(t, s"failed to parse $response")
              controller ! ParsingFailed
          }

        case Failure(t) =>
          log.error(t, "failed to send request")
          controller ! GithubRequestSendActor.RequestFailed
      }
  }
}

object GithubRequestSendActor {
  case object RequestFailed
  case object ParsingFailed
}
