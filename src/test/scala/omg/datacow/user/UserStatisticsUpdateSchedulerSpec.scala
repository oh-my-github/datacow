package omg.datacow.user

import omg.datacow.github.request._
import omg.datacow.github.response._
import omg.datacow.persistent._
import omg.datacow.util._

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest._
import scala.concurrent.duration._

import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

class UserStatisticsUpdateSchedulerSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender 
  with WordSpecLike with Matchers with BeforeAndAfterEach with BeforeAndAfterAll {

  import omg.datacow.util.Fixtures._
  import UserStatisticsUpdateActor._
  import MongoUtils._
  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  def this() = this(ActorSystem("UserStatisticsUpdaterSpecSystem"))

  val conf = ConfigFactory.load

  override def beforeEach = { TestEnvMongoUtil.dropDatabase }
  override def afterAll = { TestKit.shutdownActorSystem(system) }

  "reply GetUserRepositories if repos not exist" in {
    UserProfileDAO.insert(user1)
    UserProfileDAO.insert(user2)

    val updater = createUpdater(testActor)
    updater ! RetrieveUserAccessToken

    val expectedName1 = user1.name
    val expectedCredential1 = GithubCredential(user1.name, user1.githubProfile.accessToken)

    expectMsgPF(10 seconds) {
      case GetUserRepositories(expectedName1, expectedCredential1, _) => ()
    }

    val expectedName2 = user2.name
    val expectedCredential2 = GithubCredential(user2.name, user2.githubProfile.accessToken)

    expectMsgPF(10 seconds) {
      case GetUserRepositories(expectedName2, expectedCredential2, _) => ()
    }
  }

  "reply GetUserRepositories and GetRepositoryLanguages if the user has repos" in {
    UserProfileDAO.insert(user1)
    RepositoryDAO.insert(repo1)
    RepositoryDAO.insert(repo2)

    val updater = createUpdater(testActor)
    updater ! RetrieveUserAccessToken

    val expectedName = user1.name
    val expectedCredential = GithubCredential(user1.name, user1.githubProfile.accessToken)

    expectMsgPF(10 seconds) {
      case GetUserRepositories(expectedName, expectedCredential, _) => ()
    }

    val expectedOwner1 = repo1.owner
    val expectedRepo1 = repo1.name

    expectMsgPF(10 seconds) {
      case GetRepositoryLanguages(owner1, credential, _, repo1) => ()
        owner1 shouldBe expectedOwner1
        repo1 shouldBe expectedRepo1
    }

    val expectedOwner2 = repo2.owner
    val expectedRepo2 = repo2.name

    expectMsgPF(10 seconds) {
      case GetRepositoryLanguages(owner2, credential, _, repo2) => ()
        owner2 shouldBe expectedOwner2
        repo2 shouldBe expectedRepo2
    }
  }

  "do nothing if user collection is empty" in {
    val updater = createUpdater(testActor)
    updater ! RetrieveUserAccessToken

    expectNoMsg(2 seconds)
  }

  "getProfiles should return List[UserProfile] when user profiles exist" in {
    UserProfileDAO.insert(user1)
    UserProfileDAO.insert(user2)

    val v = UserStatisticsUpdateActor.getProfiles()
    v.isSuccess shouldBe true
    (v getOrElse Nil) shouldBe List(user1, user2)
  }

  "getProfiles should return Nil when profiles doesn't exist" in {
    val e = UserStatisticsUpdateActor.getProfiles()
    e.isFailure shouldBe true
  }

  "getRepositories should return List[Repository] when the given user have repos" in {
    UserProfileDAO.insert(user1)
    RepositoryDAO.insert(repo1)
    RepositoryDAO.insert(repo2)

    val v = getRepositories(user1)

    v map { repos => repos shouldBe List(repo1, repo2) }
    v.isSuccess shouldBe true
  }

  "getRepositories should return Nil when the given user have no repo" in {
    UserProfileDAO.insert(user3)

    val v = getRepositories(user3)

    v map { repos => repos shouldBe Nil }
    v.isFailure shouldBe true
  }


  def createUpdater(controller: ActorRef): ActorRef = {
    system.actorOf(Props(new UserStatisticsUpdateActor(controller)))
  }

}


