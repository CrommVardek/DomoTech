package be.unamur.server



// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport

// JSON handling support from Scalatra
import org.scalatra._


/**
  * Created by Axel on 10-04-16.
  */
class InsideTemperatureServlet extends ScalaWebServerStack with JacksonJsonSupport {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats


  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
   }

  get("/") {
    TemperatureData.all
  }

  //override def error(handler: ErrorHandler): Unit = ???
}




case class Temperature(value:Double, unit:String)

object TemperatureData{
  val all = List(
    Temperature(15.0,"Fahrenheit"),
    Temperature(20, "Celsius")
    )
}
