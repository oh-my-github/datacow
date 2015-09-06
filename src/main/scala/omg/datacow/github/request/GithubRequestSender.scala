package omg.datacow.github.request

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import com.typesafe.config.ConfigFactory
import omg.datacow.github.response.GithubResponse

import spray.client.pipelining._
import spray.http._
import spray.json._

import omg.datacow.github.response.GithubResponse.Protocol._
import omg.datacow.github.response.GithubResponse.APIRateLimit
import omg.datacow.github.request._

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
    case req: GithubRequest =>

      val pipeline = createPipeline(req.getCredential)
      val res = pipeline(Get(Uri(req.getUrl)))

      res.onComplete {
        case Success(response) =>
          val parsed = req match {
            case GetAPIRateLimit(_) => response.parseJson.convertTo[APIRateLimit]
            case GetRepositories(_, _) => response.parseJson.convertTo[List[Repository]]
            case GetRepositoryLanguages(owner, repository, _) =>
              val langList = response.parseJson.convertTo[List[Language]]
              Languages(owner, repository, langList)
          }

          println(parsed.toString)

        case Failure(t) =>
          println(t.getMessage)
      }
  }
}
