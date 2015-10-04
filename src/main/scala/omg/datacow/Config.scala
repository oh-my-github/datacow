package omg.datacow

import com.typesafe.config.ConfigFactory

object Config {
  val conf = ConfigFactory.load()
  val appEnv = sys.env("DATACOW_ENV")
  def getAppEnv = appEnv
}
