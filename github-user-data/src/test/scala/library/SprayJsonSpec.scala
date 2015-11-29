package library

import org.scalatest.{FunSuite, Matchers}
import spray.json.{DefaultJsonProtocol, _}

class SprayJsonSpec extends FunSuite with Matchers {

  case class Person(name: String, age: Int, address: String)
  object PersonJsonProtocol extends DefaultJsonProtocol {
    implicit val personFormat = jsonFormat3(Person)
  }

  case class Student(person: Person, school: String)
  object StudentJsonProtocol extends DefaultJsonProtocol {
    import PersonJsonProtocol._

    implicit val studentFormat = jsonFormat2(Student)
  }

  case class Teacher(name: String, school: String)
  object TeacherJsonProtocol extends DefaultJsonProtocol {

    import PersonJsonProtocol._

    implicit val teacherFormat = new RootJsonFormat[Teacher] {
      override def read(json: JsValue): Teacher =
        json.asJsObject.getFields("person", "school") match {
          case Seq(person, JsString(school)) =>
            Teacher(person.convertTo[Person].name, school)
          case _ => deserializationError("missing expected fields")
        }

      override def write(t: Teacher): JsValue =
        JsObject(
          "name" -> JsString(t.name),
          "school" -> JsString(t.school)
        )
    }
  }

  test("unmarshalling person") {
    import PersonJsonProtocol._

    val p: String =
      """
        |{
        | "name": "1ambda",
        | "age": 5,
        | "address": "Seoul"
        |}
      """.stripMargin

    val unmarshalled: Person = p.parseJson.convertTo[Person]

    unmarshalled shouldEqual Person("1ambda", 5, "Seoul")
  }

  test("unmarshall nested json") {

    import StudentJsonProtocol._

    val s: String =
      """
      |{
      | "person": {
      |   "name": "1ambda",
      |   "age": 5,
      |   "address": "Seoul"
      | },
      |
      | "school": "Skku"
      |}
    """.stripMargin

    val unmarshalled : Student = s.parseJson.convertTo[Student]

    unmarshalled shouldEqual Student(Person("1ambda", 5, "Seoul"), "Skku")
  }

  test("unmarshalling string using custom protocol") {
    import TeacherJsonProtocol._

    val s =
      """
        |{
        | "person": {
        |   "name": "1ambda",
        |   "age": 5,
        |   "address": "Seoul"
        | },
        | "school": "skku"
        |}
      """.stripMargin

    val t = s.parseJson.convertTo[Teacher]
    t shouldEqual Teacher("1ambda", "skku")
  }

}
