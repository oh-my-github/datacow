package io.github.omg.datacow.github.response

import org.scalatest.{FunSuite, Matchers}
import spray.json._

class GithubResponseSpec extends FunSuite with Matchers {
  import GithubResponse._

  test("unmarshall /rate_limit") {
    import GithubResponse.Protocol._

    val response =
      """
        | {
        |  "resources": {
        |    "core": {
        |      "limit": 5000,
        |      "remaining": 5000,
        |      "reset": 1440690501
        |    },
        |    "search": {
        |      "limit": 30,
        |      "remaining": 30,
        |      "reset": 1440686961
        |    }
        |  },
        |  "rate": {
        |    "limit": 5000,
        |    "remaining": 5000,
        |    "reset": 1440690501
        |  }
        |}
      """.stripMargin

    val rateLimit = response.parseJson.convertTo[APIRateLimit]
    println(rateLimit)
  }
}
