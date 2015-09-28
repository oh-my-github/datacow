package omg.datacow

import com.typesafe.config.ConfigFactory

object DataCowConfig {

  val conf = ConfigFactory.load
  val appEnv = sys.env("DATACOW_ENV")

  def getAppEnv      = appEnv
  def getMongoURL    = conf.getString(s"env.${appEnv}.mongoURL")
  def getMongoSchema = conf.getString(s"env.${appEnv}.mongoSchema")
}
