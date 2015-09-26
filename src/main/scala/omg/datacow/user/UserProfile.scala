package omg.datacow.user

import com.github.nscala_time.time.Imports.DateTime

case class UserProfile(
  id: String,
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

