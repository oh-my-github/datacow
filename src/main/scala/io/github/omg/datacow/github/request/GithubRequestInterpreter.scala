package io.github.omg.datacow.github.request

import akka.actor.{Actor, ActorLogging}

import scalaz._

object GithubRequestInterpreter
  extends (GithubRequest ~> Id.Id) {

  import Id._

  override def apply[A](fa: GithubRequest[A]): Id[A] = fa match {
    case GetUserRepositoryNames(userId) =>
      println(s"get user repository nams $userId")
      List("scala", "akka", "haskell")

    case GetRepositoryStatus(userId, repos) =>
      println(s"get user repo status $userId/$repos")
      List("good", "well", "poor")

    case GetRateLimit(id, accessToken) => ???
  }
}
