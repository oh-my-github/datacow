package omg.datacow.util

import omg.datacow.github.response._

object UserStatFixture {

  val langs1 = Languages(
    "2015-09-20T22:50:08.699+09:00", "1ambda", "scala",
    List(
      Language("scala", 30114),
      Language("haskell", 20104),
      Language("lisp", 3014)
    ))

  val langs2 = Languages(
    "2015-09-21T22:50:08.699+09:00", "1ambda", "scala",
    List(
      Language("scala", 30114),
      Language("haskell", 20104),
      Language("lisp", 3014)
    ))

  val repo1 = Repository(
    "2015-09-07T22:50:08.699+09:00",
    "1ambda", "scala", "1ambda/scala", false, false,
    "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)

  val repo2 = Repository(
    "2015-09-07T22:50:08.699+09:00",
    "1ambda", "scala", "1ambda/haskell", false, false,
    "2015-09-08", "2015-09-08", "2015-09-09", 10L, 1L, 2L)
}
