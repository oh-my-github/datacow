package omg.datacow.github.response

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

case class TestData(a: Int, b: String, c: List[Double])

class GithubResponseProcessor(host : String, port: Int) extends Actor {

  val conn: MongoClient = MongoClient(host, port)
  val col: MongoCollection = conn("omg")("github")

  override def receive: Receive = {
    case "hello" => sender ! "hello"
    case "insert" =>
      val t = TestData(1, "asdasdasd", List(3.5, 1.4))
      val dbo = grater[TestData].asDBObject(t)

      col.insert(dbo)
  }
}
