package omg.datacow.user

import omg.datacow.github.request.{GetRepositoryLanguages, GithubCredential, GetUserRepositories}

import scala.concurrent.duration._

import akka.actor._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import omg.datacow.persistent.MongoConfig
import omg.datacow.github.response._

import scala.util.{Try, Success, Failure}
import scalaz._
import Scalaz._

class UserStatisticsUpdater(controller: ActorRef, config: MongoConfig) extends Actor with ActorLogging {
  import UserStatisticsUpdater._
  import GithubResponsePersister._

  import context.dispatcher

  val conn = MongoClient(config.host, config.port)
  val userColl = conn(config.schema)(UserProfile.userCollectionName)
  val langColl = conn(config.schema)(languageCollectionName)
  val repoColl = conn(config.schema)(repositoryCollectionName)

  override def receive: Receive = {
    case RetrieveUserAccessToken =>

      getUserProfiles(userColl) map { userProfiles =>
        userProfiles foreach { u =>
          /* send GetUserRepositories event */
          val credential = GithubCredential(u.name, u.githubProfile.accessToken)
          controller ! GetUserRepositories(u.name, credential)

          /* send GetRepositoryLanguages event */
          getUserRepositories(u, repoColl) map { repos =>
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

object UserStatisticsUpdater {
  sealed trait UpdaterCommand
  sealed trait UpdaterEvent

  case object RetrieveUserAccessToken extends UpdaterCommand

  def getUserProfiles(userColl: MongoCollection): \/[String, List[UserProfile]] = {
    Try {
      for {
      // TODO toStream
        dbo <- userColl.find().toList
      } yield grater[UserProfile].asObject(dbo)
    } match {
      case Success(userProfiles)  => userProfiles.right[String]
      case Failure(ex)            =>
        //        log.error(ex, "Failed to convert dbo to UserProfile")
        ex.getMessage.left[List[UserProfile]]
    }
  }

  def getUserRepositories(userProfile: UserProfile,
                          repoColl: MongoCollection): \/[String, List[Repository]] = {
    Try {
      for {
        userRepo <- repoColl.find(MongoDBObject("owner" -> userProfile.name)).toList
      } yield grater[Repository].asObject(userRepo)
    } match {
      case Success(repos) => repos.right[String]
      case Failure(ex)    =>
        //        log.error(ex, "Failed to convert dbo to Repository")
        ex.getMessage.left[List[Repository]]
    }
  }

}
