package io.github.omg.datacow

import akka.util.Timeout
import io.github.omg.datacow.github.request.{GithubRequestInterpreter, GithubRequestSender, GithubRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.pattern._

import com.typesafe.config.{Config, ConfigFactory}

import scalaz.std.scalaFuture


import scala.util.{Failure, Success}
import scalaz.{Free, ~>, Id, Coyoneda}

object DataCowApp extends App {
  import GithubRequest._

  val conf = ConfigFactory.load()
  val token = conf.getString("github.token")
  val id = conf.getString("github.id")

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(3 seconds)
  import system.dispatcher

  val sender = system.actorOf(Props[GithubRequestSender], name="sender")
  run(getAPIRateLimit(id, token))(createInterpreter(sender))

  def createInterpreter(actor: ActorRef) = new (GithubRequest ~> Future) {
    override def apply[A](fa: GithubRequest[A]): Future[A]=
      (actor ? fa).asInstanceOf[Future[A]]
  }

  def run[A](fc: Free[Requestable, A])(interpreter: (GithubRequest ~> Future)): Future[A] =
    Free.runFC(fc)(interpreter)(scalaFuture.futureInstance)
}



