package io.github.omg.datacow

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.{Props, ActorSystem}

import com.typesafe.config.{Config, ConfigFactory}


import scala.util.{Failure, Success}
import scalaz.{Free, ~>, Id, Coyoneda}

object DataCowApp extends App {
  import GithubService._

  val id = "1ambda"

  val repos = run(getUserRepositoryNames(id))
  val statuses = run(getRepositoryStatus(id))

  implicit val system = ActorSystem()

  val sender = system.actorOf(Props[GithubRequestSender], name="sender")

  sender ! "request"
}



