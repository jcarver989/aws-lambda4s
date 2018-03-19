package lambda4s

import org.json4s.{ CustomSerializer, JString, NoTypeHints }
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{ read, write }
import org.json4s.Formats

trait JSON {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)

  final def toJSON[T <: AnyRef](obj: T): String = {
    write(obj)
  }

  final def fromJSON[T <: AnyRef: Manifest](obj: String): T = {
    read[T](obj)
  }
}

object JSON extends JSON

// For Custom Case Class Serialization
trait CaseObject[T] {
  def fromJSON(item: String): T
}

/** Allows for (de)serializing a case object to/from JSON, so long as the case object Foo extends CaseObject[Foo] & it implements the fromJSON method */
class CaseObjectSerializer[T: Manifest](obj: CaseObject[T]) extends CustomSerializer[T](format => (
  { case JString(status) => obj.fromJSON(status.toLowerCase) },
  { case status: T => JString(status.getClass.getSimpleName.replace("$", "")) }))