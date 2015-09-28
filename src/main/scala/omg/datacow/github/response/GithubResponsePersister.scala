package omg.datacow.github.response

import akka.actor.{ActorLogging, Actor}
import com.mongodb.casbah.Imports._
import omg.datacow.github.response.GithubResponsePersister._
import omg.datacow.persistent._

import scala.util._

class GithubResponsePersister extends Actor with ActorLogging {
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
      sender ! GithubResponsePersister.Failed
  }
}

object GithubResponsePersister {
  sealed trait PersisterEvent
  case object Persisted extends PersisterEvent
  case object Failed extends PersisterEvent
}

