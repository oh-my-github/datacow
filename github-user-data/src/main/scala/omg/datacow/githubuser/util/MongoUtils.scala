package omg.datacow.githubuser.util

import com.mongodb.casbah.Imports._
import com.novus.salat.dao._
import com.novus.salat.global._
import com.typesafe.config.ConfigFactory
import omg.datacow.githubuser.Config
import omg.datacow.githubuser.github.response._
import omg.datacow.githubuser.user.UserProfile

object MongoUtils {

  val conf = ConfigFactory.load
  val appEnv = Config.getAppEnv

  val userCollectionName = "user"
  val languageCollectionName = "language"
  val repositoryCollectionName = "repository"

  def getMongoURL    = conf.getString(s"env.${appEnv}.mongoURL")
  def mongoSchema = conf.getString(s"env.${appEnv}.mongoSchema")
  def userColl = MongoClient(getMongoURL)(mongoSchema)(userCollectionName)
  def langColl = MongoClient(getMongoURL)(mongoSchema)(languageCollectionName)
  def repoColl = MongoClient(getMongoURL)(mongoSchema)(repositoryCollectionName)

  object UserProfileDAO
    extends SalatDAO[UserProfile, ObjectId](collection = MongoUtils.userColl)
  object LanguagesDAO
    extends SalatDAO[Languages, ObjectId](collection   = MongoUtils.langColl)
  object RepositoryDAO
    extends SalatDAO[Repository, ObjectId](collection  = MongoUtils.repoColl)
}


