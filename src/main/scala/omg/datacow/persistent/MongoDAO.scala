package omg.datacow.persistent

import omg.datacow.DataCowConfig
import omg.datacow.user.UserProfile

import com.novus.salat._
import com.novus.salat.dao._
import com.novus.salat.global._
import com.novus.salat.annotations._

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.Imports._

object UserProfileDAO extends SalatDAO[UserProfile, ObjectId](
  collection = MongoConnection()(DataCowConfig.getMongoSchema)(UserProfile.userCollectionName))
