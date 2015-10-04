package omg.datacow.user

import omg.datacow.github.request._

import akka.actor._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import omg.datacow.github.response._
import omg.datacow.util._
import omg.datacow.util.MongoUtils

import scalaz._
import Scalaz._

class UserStatisticsUpdateActor(controller: ActorRef) extends Actor with ActorLogging {
  import UserStatisticsUpdateActor._
  import context.dispatcher
  import scalaz.Validation.FlatMap._

  override def receive: Receive = {
    case RetrieveUserAccessToken =>

      getProfiles() match {
        case Success(profiles) =>

          profiles foreach { profile =>
            val userId = profile.githubProfile.login
            val credential = GithubCredential(userId, profile.githubProfile.accessToken)
            controller ! GetUserRepositories(userId, credential)

            getRepositories(profile) map(_.foreach { repo =>
              controller ! GetRepositoryLanguages(userId, repo.name, credential)
            })
          }

        case Failure(message) => log.error(message)
      }
  }
}

object UserStatisticsUpdateActor {
  sealed trait UpdaterCommand
  sealed trait UpdaterEvent

  import MongoUtils._

  case object RetrieveUserAccessToken extends UpdaterCommand

  def getProfiles(): Validation[String, List[UserProfile]] = {
    UserProfileDAO.find(MongoDBObject()).toList match {
      case profiles if profiles isEmpty => "No UserProfiles".failure
      case profiles                     => profiles.success
    }
  }

  def getRepositories(profile: UserProfile): Validation[String, List[Repository]] = {
      // TODO distinct, sort by collectedAt
    RepositoryDAO.find(ref = MongoDBObject("owner" -> profile.githubProfile.login)).toList match {
      case repos if repos isEmpty => s"No Repos for ${profile.name}".failure
      case repos                  => repos.success
    }
  }
}
