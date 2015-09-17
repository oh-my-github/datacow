package omg.datacow.user

case class User(id: String,
                name: String,
                githubProfile: GithubProfile
)

case class GithubProfile(id:              String, /* github.id */
                         name:            String, /* github.login */
                         email:           String,
                         accessToken:     String,
                         refreshToken:    String,
                         following:       Long,
                         followers:       Long,
                         createdAt:       String,
                         lastUpdatedAt:   String,
                         blog:            Option[String],
                         company:         Option[String],
                         location:        Option[String]
)

object User {
  val userCollectionName = "users"
}

