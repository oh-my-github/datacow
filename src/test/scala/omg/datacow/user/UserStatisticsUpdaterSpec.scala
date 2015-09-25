package omg.datacow.user

import omg.datacow.github.request.{GetRepositoryLanguages, GithubCredential, GetUserRepositories}

import scala.concurrent.duration._
import akka.actor._
import akka.testkit._

import com.typesafe.config.ConfigFactory

import omg.datacow.github.response._
import omg.datacow.persistent.MongoConfig
import omg.datacow.util._

import org.scalatest._

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

class UserStatisticsUpdaterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender 
  with WordSpecLike with Matchers with BeforeAndAfterEach with BeforeAndAfterAll {

  import UserProfileFixture._
  import omg.datacow.util.UserStatFixture._
  import UserStatisticsUpdater._
  import GithubResponsePersister._

  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  def this() = this(ActorSystem("UserStatisticsUpdaterSystem"))

  val conf = ConfigFactory.load
  val mongoConfig = MongoUtil.getTestMongoConfig
  var conn: MongoDB = _

  var userColl: MongoCollection = _
  var langColl: MongoCollection = _
  var repoColl: MongoCollection = _

  override def beforeEach = { 
    MongoUtil.initialize
    conn = MongoUtil.getTestEnvMongoSchema
    userColl = conn(UserProfile.userCollectionName)
    langColl = conn(languageCollectionName)
    repoColl = conn(repositoryCollectionName)
  }
  override def afterEach = { MongoUtil.stop }
  
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val user1Dbo = grater[UserProfile].asDBObject(user1)
  val user2Dbo = grater[UserProfile].asDBObject(user2)
  val user3Dbo = grater[UserProfile].asDBObject(user3)
  val lang1Dbo = grater[Languages].asDBObject(langs1)
  val lang2Dbo = grater[Languages].asDBObject(langs2)
  val repo1Dbo = grater[Repository].asDBObject(repo1)
  val repo2Dbo = grater[Repository].asDBObject(repo2)
  val repo3Dbo = grater[Repository].asDBObject(repo3)

  "reply GetUserRepositories if repos not exist" in {
    userColl.insert(user1Dbo)
    userColl.insert(user2Dbo)

    val updater = createUpdater(testActor, mongoConfig)
    updater ! RetrieveUserAccessToken

    val expectedName1 = user1.name
    val expectedCredential1 = GithubCredential(user1.name, user1.githubProfile.accessToken)

    expectMsgPF(10 seconds) {
      case GetUserRepositories(expectedName1, expectedCredential1) => ()
    }

    val expectedName2 = user2.name
    val expectedCredential2 = GithubCredential(user2.name, user2.githubProfile.accessToken)

    expectMsgPF(10 seconds) {
      case GetUserRepositories(expectedName2, expectedCredential2) => ()
    }
  }

  "reply GetUserRepositories and GetRepositoryLanguages if the user has repos" in {
    userColl.insert(user1Dbo)
    repoColl.insert(repo1Dbo)
    repoColl.insert(repo2Dbo)

    val updater = createUpdater(testActor, mongoConfig)
    updater ! RetrieveUserAccessToken

    val expectedName = user1.name
    val expectedCredential = GithubCredential(user1.name, user1.githubProfile.accessToken)

    expectMsgPF(10 seconds) {
      case GetUserRepositories(expectedName, expectedCredential) => ()
    }

    val expectedRepoOwner1 = repo1.owner
    val expectedRepoName1 = repo1.name

    expectMsgPF(10 seconds) {
      case GetRepositoryLanguages(expectedRepoOwner1, expectedRepoName1, expectedCredential) => ()
    }

    val expectedRepoOwner2 = repo2.owner
    val expectedRepoName2 = repo2.name

    expectMsgPF(10 seconds) {
      case GetRepositoryLanguages(expectedRepoOwner2, expectedRepoName2, expectedCredential) => ()
    }
  }

  "do nothing if user collection is empty" in {
    val updater = createUpdater(testActor, mongoConfig)
    updater ! RetrieveUserAccessToken

    expectNoMsg(2 seconds)
  }

  "getUserProfiles should return List[UserProfile] when user profiles exist" in {
    userColl.insert(user1Dbo)
    userColl.insert(user2Dbo)

    val e = UserStatisticsUpdater.getUserProfiles(userColl)
    e.isRight shouldBe true
    (e getOrElse Nil) shouldBe List(user1, user2)
  }

  "getUserProfiles should return Nil when profiles doesn't exist" in {
    val e = UserStatisticsUpdater.getUserProfiles(userColl)
    e.isRight shouldBe true
    (e getOrElse "empty" ) shouldBe Nil
  }

  "getUserProfiles should return failure when an exception occurred" in {
    userColl.insert(lang1Dbo)

    val e = UserStatisticsUpdater.getUserProfiles(userColl)
    e.isLeft shouldBe true
  }

  "getUserRepositories should return List[Repository] when the given user have repos" in {
    userColl.insert(user1Dbo)
    repoColl.insert(repo1Dbo)
    repoColl.insert(repo2Dbo)

    val reposEither = getUserRepositories(user1, repoColl)

    reposEither.isRight shouldBe true
    (reposEither getOrElse Nil) shouldBe List(repo1, repo2)
  }

  "getUserRepositories should return Nil when the given user have no repo" in {
    userColl.insert(user3Dbo)

    val reposEither = getUserRepositories(user3, repoColl)

    reposEither map { repos =>
      repos shouldBe Nil
    }

    reposEither.isRight shouldBe true
  }


  def createUpdater(controller: ActorRef, config: MongoConfig): ActorRef = {
    system.actorOf(Props(new UserStatisticsUpdater(controller, config)))
  }

}


