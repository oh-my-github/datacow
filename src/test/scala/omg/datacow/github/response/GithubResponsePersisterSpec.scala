package omg.datacow.github.response

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import omg.datacow.github.response.GithubResponse._
import omg.datacow.github.response.GithubResponsePersister._
import org.scalatest._

import scala.concurrent.duration._

class GithubResponsePersisterSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  def this() = this(ActorSystem("GithubResponseProcessorSpec"))

  val conf = ConfigFactory.load
  val mongoHost = conf.getString("mongo.local.host")
  val mongoPort = conf.getInt("mongo.local.port")
  val mongoSchema = conf.getString("mongo.test.db")

  override def beforeAll: Unit = {
    EmbeddedMongo.initialize
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
    EmbeddedMongo.stop
  }

  "should persist Repository to the repository collection" in {
    val persister = createPersister

    val repo1 = Repository(
      "2015-09-07T22:50:08.699+09:00",
      "1ambda", "scala", "1ambda/scala", false, false,
      "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

    val repo2 = Repository(
      "2015-09-07T22:50:08.699+09:00",
      "1ambda", "scala", "1ambda/haskell", false, false,
      "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

    persister ! Repositories(List(repo1, repo2))
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  "should persist Languages to the language collection" in {
    val persister = createPersister

    val langs = Languages(
      "2015-09-07T22:50:08.699+09:00", "1ambda", "scala",
      List(
        Language("scala", 30114),
        Language("haskell", 20104),
        Language("lisp", 3014)
      ))

    persister ! langs
    expectMsgPF(10 seconds) {
      case Persisted => ()
    }
  }

  "should return Failed when given an undefined GithubResponse" in {
    val persister = createPersister

    val emptyRate = Rate(1, 1, 1L)
    persister ! APIRateLimit(Resources(emptyRate, emptyRate), emptyRate)

    expectMsgPF(10 seconds) {
      case GithubResponsePersister.Failed => ()
    }
  }

  def createPersister =
    TestActorRef(Props(new GithubResponsePersister(mongoHost, mongoPort, mongoSchema)))

}

object EmbeddedMongo {
  import de.flapdoodle.embed.mongo._
  import de.flapdoodle.embed.mongo.config._
  import de.flapdoodle.embed.mongo.distribution._
  import de.flapdoodle.embed.process.config.io._
  import de.flapdoodle.embed.process.extract._
  import de.flapdoodle.embed.process.io._
  import de.flapdoodle.embed.process.io.directories._
  import de.flapdoodle.embed.process.runtime._

  val conf = ConfigFactory.load
  val mongoHost = conf.getString("mongo.test.host")
  val mongoPort = conf.getInt("mongo.test.port")

  lazy val localhostIPv6 = Network.localhostIsIPv6()
  lazy val processOutput = new ProcessOutput(
    Processors.named("[mongod>]", new NullProcessor),
    Processors.named("[MONGOD>]", new NullProcessor),
    Processors.named("[console>]", new NullProcessor))
  val command = Command.MongoD
  lazy val artifactStorePath = new PlatformTempDir()
  lazy val executableNaming = new UUIDTempNaming()

  lazy val runtimeConfig = new RuntimeConfigBuilder()
    .defaults(command)
//    .processOutput(processOutput)
    .artifactStore(
      new ExtractedArtifactStoreBuilder()
        .defaults(command)
        .download(
          new DownloadConfigBuilder()
            .defaultsForCommand(command)
            .artifactStorePath(artifactStorePath))
        .executableNaming(executableNaming))
    .build()

  val version = Version.Main.PRODUCTION
  lazy val mongodConfig = new MongodConfigBuilder()
    .version(version)
    .net(new Net(mongoPort, localhostIPv6))
    .cmdOptions(
      new MongoCmdOptionsBuilder()
        .syncDelay(1)
        .useNoPrealloc(false)
        .useSmallFiles(false)
        .useNoJournal(false)
        .enableTextSearch(true)
        .build())
    .build()

  lazy val mongodStarter = MongodStarter.getInstance(runtimeConfig)
  lazy val mongod = mongodStarter.prepare(mongodConfig)
  lazy val mongodExe = mongod.start()

  def initialize = { mongodExe }
  def stop = {
    mongod.stop
    mongodExe.stop
  }
}
