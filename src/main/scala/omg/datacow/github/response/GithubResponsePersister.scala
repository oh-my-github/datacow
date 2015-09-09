package omg.datacow.github.response

import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import omg.datacow.github.response.GithubResponse._
import omg.datacow.github.response.GithubResponsePersister._

import scala.util._
import scala.reflect.{ClassTag, classTag}

case class TestData(a: Int, b: String, c: List[Double])

class GithubResponsePersister(host : String, port: Int, schema: String) extends Actor with ActorLogging {

  val languageCollectionName = "language" 
  val repositoryCollectionName = "repository"
  
  lazy val conn: MongoClient = MongoClient(host, port)
  lazy val languages: MongoCollection = conn(schema)(languageCollectionName)
  lazy val repositories: MongoCollection =  conn(schema)(repositoryCollectionName)


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

    case _ =>
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

