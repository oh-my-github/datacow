package omg.datacow.github.response

import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import omg.datacow.DataCowConfig
import omg.datacow.github.response.GithubResponse._
import omg.datacow.github.response.GithubResponsePersister._
import omg.datacow.persistent.MongoUtils

import scala.util._

class GithubResponsePersister extends Actor with ActorLogging {
  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  implicit val writeConcern = WriteConcern.JournalSafe

  val languages    = MongoUtils.getLanguageCollection
  val repositories = MongoUtils.getRepositoryCollection

  override def receive: Receive = {
    case Repositories(repos) =>

      // TODO check empty list
      // TODO transaction
      val result: List[Try[WriteResult]] = repos map { repo =>
        Try(repositories.insert(grater[Repository].asDBObject(repo)))
      }

      sequence(result) match {
        case Success(_) =>
          sender ! Persisted

        case Failure(t: Throwable) =>
          log.error(t, "failed to serialize repository list " + repos)
          sender ! Failed
      }

    case langs: Languages =>
      val dbo = grater[Languages].asDBObject(langs)
      languages.insert(dbo)
      sender ! Persisted

    case message =>
      sender ! GithubResponsePersister.Failed
  }

  def sequence[A](xs: Seq[Try[A]]) =
    Try(xs.map(_.get))
}

object GithubResponsePersister {
  sealed trait PersisterEvent
  case object Persisted extends PersisterEvent
  case object Failed extends PersisterEvent
}

