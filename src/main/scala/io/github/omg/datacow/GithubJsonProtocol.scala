package io.github.omg.datacow

import org.json4s.{Formats, DefaultFormats}
import spray.httpx.Json4sSupport

object GithubJsonProtocol extends Json4sSupport {
  override implicit  def json4sFormats: Formats = DefaultFormats
}
