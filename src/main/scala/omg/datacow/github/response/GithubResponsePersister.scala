package omg.datacow.github.response

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import omg.datacow.github.response.GithubResponse._
import omg.datacow.github.response.GithubResponsePersister._

case class TestData(a: Int, b: String, c: List[Double])

class GithubResponsePersister(host : String, port: Int, schema: String) extends Actor {

  val languageCollectionName = "language" 
  val repositoryCollectionName = "repository"
  
  lazy val conn: MongoClient = MongoClient(host, port)
  lazy val languages: MongoCollection = conn(schema)(languageCollectionName)
  lazy val repositories: MongoCollection =  conn(schema)(repositoryCollectionName)


  override def receive: Receive = {

    case res: GithubResponse =>
      val controller = sender
      res match {
        case repo: Repository =>
          val dbo = grater[Repository].asDBObject(repo)
          repositories.insert(dbo)
          controller ! Persisted
        case langs: Languages =>
          val dbo = grater[Languages].asDBObject(langs)
          languages.insert(dbo)
          controller ! Persisted
        case _ =>
          controller ! Failed
      }
  }
}

object GithubResponsePersister {
  sealed trait PersisterEvent
  case object Persisted extends PersisterEvent
  case object Failed extends PersisterEvent
}

