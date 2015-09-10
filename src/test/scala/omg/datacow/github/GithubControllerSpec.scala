package omg.datacow.github

import akka.actor.ActorSystem
import akka.testkit._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoCollection, MongoClient}
import com.typesafe.config.ConfigFactory
import omg.datacow.github.request._
import omg.datacow.github.response._
import omg.datacow.util.MongoUtil
import org.scalatest._
import omg.datacow.github.response.GithubResponsePersister._

import scala.concurrent.duration._

class GithubControllerSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  val conf = ConfigFactory.load
  val testCredential = GithubCredential(
    conf.getString("github.id"),
    conf.getString("github.token"))

  lazy val conn = MongoUtil.getTestEnvMongoSchema
  lazy val languages: MongoCollection = conn(languageCollectionName)
  lazy val repositories: MongoCollection =  conn(repositoryCollectionName)

  def this() = this(ActorSystem("ControllerSpec"))

  override def beforeAll() = {
    MongoUtil.initialize
  }

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
    MongoUtil.stop
  }

  "controller should persiste Languages when given a Languages case class" in {
    val controller = TestActorRef[GithubController]
    val langs = Languages(
      "2015-09-07T22:50:08.699+09:00", "1ambda", "scala",
      List(
        Language("scala", 30114),
        Language("haskell", 20104),
        Language("lisp", 3014)
      ))
//
    controller ! langs
//    languages.findOne(MongoDBObject("owner" -> "1ambda"))
  }

}
