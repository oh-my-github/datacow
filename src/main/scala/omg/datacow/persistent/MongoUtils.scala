package omg.datacow.persistent

import com.novus.salat._
import com.novus.salat.dao._
import com.novus.salat.global._
import com.novus.salat.annotations._

import com.mongodb.casbah.Imports._

import com.typesafe.config.ConfigFactory
import omg.datacow.github.response.{Repository, Languages}
import omg.datacow.user.UserProfile

object MongoUtils {
  import omg.datacow.DataCowConfig._

  val conf = ConfigFactory.load
  val appEnv = getAppEnv

  val userCollectionName = "users"
  val languageCollectionName = "language"
  val repositoryCollectionName = "repository"

  def getMongoURL    = conf.getString(s"env.${appEnv}.mongoURL")
  def getMongoSchema = conf.getString(s"env.${appEnv}.mongoSchema")
  def getUserCollection = MongoClient(getMongoURL)(getMongoSchema)(userCollectionName)
  def getLanguageCollection = MongoClient(getMongoURL)(getMongoSchema)(languageCollectionName)
  def getRepositoryCollection = MongoClient(getMongoURL)(getMongoSchema)(repositoryCollectionName)
}

object UserProfileDAO
  extends SalatDAO[UserProfile, ObjectId](collection = MongoUtils.getUserCollection)
object LanguagesDAO
  extends SalatDAO[Languages, ObjectId](collection   = MongoUtils.getLanguageCollection)
object RepositoryDAO
  extends SalatDAO[Repository, ObjectId](collection  = MongoUtils.getRepositoryCollection)

