package omg.datacow.github.response

import spray.json._

import GithubResponse._
sealed trait GithubResponse

object GithubResponse {
  object Protocol extends DefaultJsonProtocol {
    implicit val rateFormat = jsonFormat3(Rate)
    implicit val resourceFormats = jsonFormat2(Resources)
    implicit val apiRateLimitFormat = jsonFormat2(APIRateLimit)

    implicit val repositoryFormat = new RootJsonFormat[Repository] {
      override def read(json: JsValue): Repository =
        json.asJsObject.getFields(
          "full_name", "html_url", "private", "fork",
          "created_at", "updated_at", "pushed_at",
          "stargazers_count", "watchers_count", "forks") match {
          case Seq(JsString(full_name), JsString(url),
                    JsBoolean(isPrivate), JsBoolean(isForked),
                    JsString(createdAt), JsString(updatedAt), JsString(pushedAt),
                    JsNumber(stargazersCount), JsNumber(watchersCount), JsNumber(forksCount)) =>

            val (owner, name) = full_name.split("/") match {
              case Array(owner, repoName) => (owner, repoName)
            }

            Repository(
              owner, name, url,
              isForked, isPrivate, createdAt, updatedAt, pushedAt,
              stargazersCount.toLong, watchersCount.toLong, forksCount.toLong
            )

          case _ => deserializationError("error occurred while parsing repository")
        }

      override def write(obj: Repository): JsValue =
        obj.toString.parseJson
    }

    implicit val languageListFommat = new RootJsonFormat[List[Language]] {
      override def read(json: JsValue): List[Language] =
        json.asJsObject.fields.map {
          case (lang, line) => Language(lang, BigInt(line.toString))
        } toList

      override def write(obj: List[Language]): JsValue =
        obj.toString.parseJson
    }
  }

  sealed case class Resources(core: Rate, search: Rate)
  sealed case class Rate(limit: Int, remaining: Int, reset: Long)
  final case class APIRateLimit(resources: Resources, rate: Rate) extends GithubResponse

  sealed case class Repository(owner: String, name: String, url: String,
                               isPrivate: Boolean, isForked: Boolean,
                               createdAt: String, updatedAt: String, pushedAt: String,
                               stargazersCount: Long, watchersCount: Long, forksCount: Long)

  sealed case class Language(name: String, line: BigInt)
  final case class Languages(owner: String, repositoryName: String,
                             languages: List[Language]) extends GithubResponse
}
