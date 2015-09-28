package omg.datacow.user

import omg.datacow.github.request._

import akka.actor._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import omg.datacow.github.response._
import omg.datacow.persistent._

import scala.util.{Try, Success, Failure}
import scalaz._
import Scalaz._

class UserStatisticsUpdateScheduler(controller: ActorRef) extends Actor with ActorLogging {
  import UserStatisticsUpdateScheduler._
  import context.dispatcher

  override def receive: Receive = {
    case RetrieveUserAccessToken =>
      getUserProfiles() map { userProfiles =>
        userProfiles foreach { u =>
          /* send GetUserRepositories event */
          val credential = GithubCredential(u.name, u.githubProfile.accessToken)
          controller ! GetUserRepositories(u.name, credential)

          /* send GetRepositoryLanguages event */
          getUserRepositories(u) map { repos =>
            for {
              r <- repos
            } yield {
              controller ! GetRepositoryLanguages(u.name, r.name, credential)
            }
          }
        }
      }
  }
}

object UserStatisticsUpdateScheduler {
  sealed trait UpdaterCommand
  sealed trait UpdaterEvent

  import MongoUtils._

  case object RetrieveUserAccessToken extends UpdaterCommand

  def getUserProfiles(): \/[String, List[UserProfile]] = {
    Try {
      // TODO: toStream
      UserProfileDAO.find(MongoDBObject()).toList
    } match {
      case Success(userProfiles)  => userProfiles.right[String]
      case Failure(ex)            =>
        //        log.error(ex, "Failed to convert dbo to UserProfile")
        ex.getMessage.left[List[UserProfile]]
    }
  }

  def getUserRepositories(userProfile: UserProfile): \/[String, List[Repository]] = {
    Try {
      // TODO toStream
      // TODO distinct, sort by collectedAt
      RepositoryDAO.find(ref = MongoDBObject("owner" -> userProfile.name)).toList
    } match {
      case Success(repos) => repos.right[String]
      case Failure(ex)    =>
        //        log.error(ex, "Failed to convert dbo to Repository")
        ex.getMessage.left[List[Repository]]
    }
  }

}
