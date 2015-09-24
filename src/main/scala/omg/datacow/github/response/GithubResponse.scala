package omg.datacow.github.response

import spray.json._

import com.github.nscala_time.time.Imports._
import org.joda.time.Days
import org.joda.time.format._

import scala.util.Try

import omg.datacow.github.request._


trait GithubResponse
sealed case class Resources(core: Rate, search: Rate)
sealed case class Rate(limit: Int, remaining: Int, reset: Long)
final case class APIRateLimit(resources: Resources, rate: Rate) extends GithubResponse

final case class Repository(collectedAt: DateTime,
                            owner: String, name: String, url: String,
                            isPrivate: Boolean, isForked: Boolean,
                            createdAt: String, updatedAt: String, pushedAt: String,
                            stargazersCount: Long, watchersCount: Long, forksCount: Long)

final case class Repositories(repos: List[Repository]) extends GithubResponse // avoid to type erasure

final case class Language(name: String, line: Long)
final case class Languages(collectedAt: DateTime,
                           owner: String, repositoryName: String,
                           languages: List[Language]) extends GithubResponse

object GithubResponse {
  case object ParsingFailed
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
              getCurrentDateTimeAsISOString, owner, name, url,
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
          case (lang, line) => Language(lang, line.toString.toLong)
        } toList

      override def write(obj: List[Language]): JsValue =
        obj.toString.parseJson
    }
  }


  def getCurrentDateTimeAsISOString: DateTime = {
    DateTime.now
  }

  def parseGithubResponse(request: GithubRequest, response: String) = {
    import Protocol._

    Try {
      request match {
        case GetAPIRateLimit(_) => response.parseJson.convertTo[APIRateLimit]
        case GetUserRepositories(_, _) =>
          Repositories(response.parseJson.convertTo[List[Repository]])
        case GetRepositoryLanguages(owner, repository, _) =>
          val langList = response.parseJson.convertTo[List[Language]]
          Languages(GithubResponse.getCurrentDateTimeAsISOString, owner, repository, langList)
      }
    }
  }
}

