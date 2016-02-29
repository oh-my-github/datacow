package omg.datacow.githubuser.util

import com.mongodb.casbah.{MongoDB, MongoClient}
import com.typesafe.config.ConfigFactory
import MongoUtils._

import de.flapdoodle.embed.mongo._
import de.flapdoodle.embed.mongo.config._
import de.flapdoodle.embed.mongo.distribution._
import de.flapdoodle.embed.process.config.io._
import de.flapdoodle.embed.process.extract._
import de.flapdoodle.embed.process.io._
import de.flapdoodle.embed.process.io.directories._
import de.flapdoodle.embed.process.runtime._

object TestEnvMongoUtil {

  val conf = ConfigFactory.load
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
        .processOutput(processOutput)
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
  val testMongoPort = getMongoURL.split(":")(1)
  lazy val mongodConfig = new MongodConfigBuilder()
    .version(version)
    .net(new Net(testMongoPort.toInt, localhostIPv6))
    .cmdOptions(
      new MongoCmdOptionsBuilder()
        .syncDelay(1)
        .useNoPrealloc(false)
        .useSmallFiles(false)
        .useNoJournal(false)
        .enableTextSearch(true)
        .build())
    .build()

  var mongod: Option[MongodExecutable] = None
  var mongodExe: Option[MongodProcess] = None

  private def initialize: Unit = {
    stop /* stop before running */
    mongod =
      Some(MongodStarter.getInstance(runtimeConfig).prepare(mongodConfig))
    mongodExe = mongod.map { _.start() }
  }
  private def stop = {
    mongod.foreach( _.stop )
    mongodExe.foreach { _.stop() }
  }

  def getMongoDB: MongoDB = MongoClient(getMongoURL)(mongoSchema)
  def dropDatabase = getMongoDB.dropDatabase

  initialize
}
