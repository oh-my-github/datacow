package omg.datacow.github.response

import akka.actor._
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest._

class GithubResponseProcessorSpec(_system: ActorSystem)
  extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  def this() = this(ActorSystem("GithubResponseProcessorSpec"))

  val conf = ConfigFactory.load
  val mongoHost = conf.getString("mongo.test.host")
  val mongoPort = conf.getInt("mongo.test.port")

  override def beforeAll: Unit = {
    EmbeddedMongo.initialize
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
    EmbeddedMongo.stop
  }

  "processor should persist message" in {
    val processor = TestActorRef(Props(new GithubResponseProcessor(mongoHost, mongoPort)), name = "processor")
    val message = "hello"
    processor ! message
  }
}

object EmbeddedMongo {
  import de.flapdoodle.embed.mongo._
  import de.flapdoodle.embed.mongo.distribution._
  import de.flapdoodle.embed.mongo.config._
  import de.flapdoodle.embed.process.runtime._
  import de.flapdoodle.embed.process.io._
  import de.flapdoodle.embed.process.config.io._
  import de.flapdoodle.embed.process.io.directories._
  import de.flapdoodle.embed.process.extract._

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
