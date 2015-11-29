package omg.datacow.github.response

import spray.json._

import com.github.nscala_time.time.Imports.DateTime
import org.joda.time.format._

import com.novus.salat._
import com.novus.salat.dao._
import com.novus.salat.global._
import com.novus.salat.annotations._

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.Imports._

import scala.util.Try

import omg.datacow.github.request._

trait GithubResponse
sealed case class Resources(core: Rate, search: Rate)
sealed case class Rate(limit: Int, remaining: Int, reset: Long)
final case class APIRateLimit(resources: Resources, rate: Rate) extends GithubResponse

final case class Repository(collectAt: DateTime,
                            owner: String, name: String, url: String,
                            isPrivate: Boolean, isForked: Boolean,
                            createdAt: DateTime, updatedAt: DateTime, pushedAt: DateTime,
                            stargazersCount: Long, watchersCount: Long, forksCount: Long)

final case class Repositories(repos: List[Repository]) extends GithubResponse // avoid to type erasure

final case class Language(name: String, line: Long)
final case class Languages(collectAt: DateTime,
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
              /* TODO: useless, inefficient */ DateTime.now,  owner, name, url,
              isForked, isPrivate,
              new DateTime(createdAt), new DateTime(updatedAt), new DateTime(pushedAt),
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

  def parseGithubResponse(request: GithubRequest, response: String) = {
    import Protocol._

    Try {
      request match {
        case GetAPIRateLimit(_, _) => response.parseJson.convertTo[APIRateLimit]
        case GetUserRepositories(owner, credential, collectAt) =>
          Repositories(
            /* TODO: inefficient */
            response.parseJson.convertTo[List[Repository]] map { repo =>
              repo.copy(collectAt = collectAt)
            }
          )
        case GetRepositoryLanguages(owner, credential, collectAt, repository) =>
          val langList = response.parseJson.convertTo[List[Language]]
          Languages(collectAt, owner, repository, langList)
      }
    }
  }
}

