package io.github.omg.datacow.github.response

import spray.json.DefaultJsonProtocol

import GithubResponse._

sealed trait GithubResponse

object GithubResponse {
    object Protocol extends DefaultJsonProtocol {
      implicit val rateFormat = jsonFormat3(Rate)
      implicit val resourceFormats = jsonFormat2(Resources)
      implicit val apeRateLimitFormat = jsonFormat2(APIRateLimit)

    }

  sealed case class Resources(core: Rate, search: Rate)
  sealed case class Rate(limit: Int, remaining: Int, reset: Long)
  final case class APIRateLimit(resources: Resources, rate: Rate) extends GithubResponse
}

