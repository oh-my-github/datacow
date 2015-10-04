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

class UserStatisticsUpdateActor(controller: ActorRef) extends Actor with ActorLogging {
  import UserStatisticsUpdateActor._
  import context.dispatcher

  override def receive: Receive = {
    case RetrieveUserAccessToken =>
      getUserProfiles() map { userProfiles =>
        userProfiles foreach { u =>
          /* send GetUserRepositories event */

          val userId = u.githubProfile.login
          val credential = GithubCredential(userId, u.githubProfile.accessToken)
          controller ! GetUserRepositories(userId, credential)

          /* send GetRepositoryLanguages event */
          println("asd")
          getUserRepositories(u) map { repos =>
            println(s"repos: $repos")
            for {
              r <- repos
            } yield {
              controller ! GetRepositoryLanguages(userId, r.name, credential)
            }
          }
        }
      }
  }
}

object UserStatisticsUpdateActor {
  sealed trait UpdaterCommand
  sealed trait UpdaterEvent

  import MongoUtils._

  case object RetrieveUserAccessToken extends UpdaterCommand

  def getUserProfiles(): \/[String, List[UserProfile]] = {
    Try {
      // TODO: toStream
      // TODO logging
      UserProfileDAO.find(MongoDBObject()).toList
    } match {
      case Success(userProfiles)  => userProfiles.right[String]
      case Failure(ex)            =>
        println(ex)
        ex.getMessage.left[List[UserProfile]]
    }
  }

  def getUserRepositories(userProfile: UserProfile): \/[String, List[Repository]] = {
    Try {
      // TODO toStream
      // TODO distinct, sort by collectedAt
      RepositoryDAO.find(ref = MongoDBObject("owner" -> userProfile.githubProfile.login)).toList
    } match {
      case Success(repos) => repos.right[String]
      case Failure(ex)    =>
        println(ex)
        ex.getMessage.left[List[Repository]]
    }
  }

}
