package omg.datacow.githubuser.github.response

import akka.actor.{ActorLogging, Actor}
import com.mongodb.casbah.Imports._
import omg.datacow.githubuser.github.response.GithubResponsePersistActor._
import omg.datacow.githubuser.util._
import omg.datacow.githubuser.util.MongoUtils

import scala.util._

class GithubResponsePersistActor extends Actor with ActorLogging {
  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  import MongoUtils._
  implicit val writeConcern = WriteConcern.JournalSafe

  override def receive: Receive = {
    case Repositories(repos) =>

      // TODO check empty list
      // TODO transaction
      Try(RepositoryDAO.insert(repos)) match {
        case Success(_)  =>
          sender ! Persisted

        case Failure(ex) =>
          log.error(ex, s"failed to save Repositories:\n$repos")
          sender ! Failed
      }

    case langs: Languages =>
      Try(LanguagesDAO.insert(langs)) match {
        case Success(_)  =>
          sender ! Persisted

        case Failure(ex) =>
          log.error(ex, s"failed to save Languages:\n$langs")
          sender ! Failed
      }

    case message =>
      sender ! GithubResponsePersistActor.Failed
  }
}

object GithubResponsePersistActor {
  sealed trait PersisterEvent
  case object Persisted extends PersisterEvent
  case object Failed extends PersisterEvent
}

