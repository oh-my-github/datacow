package omg.datacow.githubuser.user

import omg.datacow.githubuser.github.request._
import omg.datacow.githubuser.github.response._
import omg.datacow.githubuser.util.MongoUtils

import akka.actor._
import com.mongodb.casbah.Imports._
import com.github.nscala_time.time.Imports.DateTime

import scalaz._, Scalaz._

class UserStatisticsUpdateActor(controller: ActorRef) extends Actor with ActorLogging {
  import UserStatisticsUpdateActor._

  override def receive: Receive = {
    case RetrieveUserAccessToken =>

      getProfiles() match {
        case Success(profiles) =>

          val updateAt = DateTime.now

          profiles foreach { profile =>
            val userId = profile.githubProfile.login
            val credential = GithubCredential(userId, profile.githubProfile.accessToken)
            controller ! GetUserRepositories(userId, credential, updateAt)

            getRepositories(profile) map(_.foreach { repo =>
              controller ! GetRepositoryLanguages(userId, credential, updateAt, repo.name)
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
