package omg.datacow.user

import com.github.nscala_time.time.Imports.DateTime

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class UserProfile(
  _id: ObjectId = new ObjectId,
  name: String,
  githubProfile: GithubProfile
)

case class GithubProfile(
  id:            String, /* github.id */
  login:         String, /* github.login */
  email:         String,
  accessToken:   String,
  refreshToken:  String,
  following:     Long,
  followers:     Long,
  createdAt:     DateTime,
  updatedAt:     DateTime,
  blog:          Option[String],
  company:       Option[String],
  location:      Option[String]
)

object UserProfile {
  val userCollectionName = "users"
}

