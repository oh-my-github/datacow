package omg.datacow.util

import omg.datacow.github.response._
import com.github.nscala_time.time.Imports.DateTime
import scala.concurrent.duration._

object UserStatFixture {

  val langs1 = Languages(
    DateTime.now,
    "1ambda", "scala",
    List(
      Language("scala", 30114),
      Language("haskell", 20104),
      Language("lisp", 3014)
    ))

  val langs2 = Languages(
    DateTime.now,
    "1ambda", "scala",
    List(
      Language("scala", 30114),
      Language("haskell", 20104),
      Language("lisp", 3014)
    ))

  val repo1 = Repository(
    DateTime.now,
    "1ambda", "scala", "1ambda/scala", false, false,
    "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

  val repo2 = Repository(
    DateTime.now,
    "1ambda", "scala", "1ambda/haskell", false, false,
    "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)
}
