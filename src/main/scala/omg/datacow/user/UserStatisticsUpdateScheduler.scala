package omg.datacow.user

import com.typesafe.config.ConfigFactory
import omg.datacow.DataCowConfig
import omg.datacow.github.request.{GetRepositoryLanguages, GithubCredential, GetUserRepositories}

import akka.actor._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import omg.datacow.github.response._
import omg.datacow.persistent.MongoUtils

import scala.util.{Try, Success, Failure}
import scalaz._
import Scalaz._

class UserStatisticsUpdateScheduler(controller: ActorRef) extends Actor with ActorLogging {
  import UserStatisticsUpdateScheduler._
  import context.dispatcher

  val userColl = MongoUtils.getUserCollection
  val langColl = MongoUtils.getLanguageCollection
  val repoColl = MongoUtils.getRepositoryCollection

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

object UserStatisticsUpdateScheduler {
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
