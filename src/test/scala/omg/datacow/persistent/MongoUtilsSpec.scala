package omg.datacow.persistent

import com.mongodb.casbah.commons.MongoDBObject
import omg.datacow.util.TestEnvMongoUtil
import org.scalatest._

class MongoUtilsSpec extends WordSpecLike with Matchers with BeforeAndAfterEach {
  import com.mongodb.casbah.commons.conversions.scala._
  RegisterJodaTimeConversionHelpers()

  import omg.datacow.util.Fixtures._

  override def beforeEach = {
    TestEnvMongoUtil.initialize
  }

  override def afterEach = { TestEnvMongoUtil.stop }

  "test UserProfileDAO" in {
    UserProfileDAO.insert(user1)
    val found = UserProfileDAO.findOneById(id = user1._id)
    found shouldBe Some(user1)
  }

  "test RepositoryDAO" in {
    RepositoryDAO.insert(repo1)
    val found = RepositoryDAO.findOne(MongoDBObject("url" -> repo1.url))
    found shouldBe Some(repo1)
  }

  "test LangaugeDAO" in {
    LanguagesDAO.insert(langs1)
    val found = LanguagesDAO.findOne(
      MongoDBObject("owner" -> "1ambda", "repositoryName" -> "scala"))
    found shouldBe Some(langs1)
  }
}


