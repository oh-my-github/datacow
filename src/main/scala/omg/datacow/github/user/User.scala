package omg.datacow.github.user

case class User(name: String,
                githubProfile: GithubProfile
){}

case class GithubProfile(userName: String,
                        email: String,
                        accessToken: String,
                        refreshToken: String)

/**
 *
 * {
           name: profile.displayName,
           email: profile.emails[0].value,
           role: 'user',
           username: profile.username,
           provider: 'github',
           github: profile._json,
           github_accessToken: accessToken,
           github_refreshTocken: refreshToken
}
 */
