package io.github.omg.datacow

import akka.util.Timeout
import io.github.omg.datacow.github.request.{GithubCredential, GetAPIRateLimit, GithubRequestSender, GithubRequest}

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

  sender ! GetAPIRateLimit(GithubCredential(id, token))
}



