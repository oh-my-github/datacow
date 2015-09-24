package omg.datacow.user

import scala.concurrent.duration._

import akka.actor._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import omg.datacow.persistent.MongoConfig
import omg.datacow.github.response.GithubResponsePersister

class UserStatisticsUpdater(controller: ActorRef, config: MongoConfig) extends Actor with ActorLogging {
  import UserStatisticsUpdater._
  import GithubResponsePersister._

  import context.dispatcher

  val conn = MongoClient(config.host, config.port)
  val userColl = conn(config.schema)(UserProfile.userCollectionName)
  val langColl = conn(config.schema)(languageCollectionName)
  val repoColl = conn(config.schema)(repositoryCollectionName)

//  context.system.scheduler.schedule(5 seconds , 60 seconds, self, RetrieveUserAccessToken)

  override def receive: Receive = {
    case RetrieveUserAccessToken => ???
    case UpdateStatistics(id, token) => ???
  }


}

object UserStatisticsUpdater {
  sealed trait UpdaterCommand
  sealed trait UpdaterEvent

  case object RetrieveUserAccessToken                     extends UpdaterCommand
  case class  UpdateStatistics(id: String, token: String) extends UpdaterCommand
}
