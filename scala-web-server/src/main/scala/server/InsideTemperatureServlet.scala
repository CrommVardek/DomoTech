package server

import actors.{HeatManagerActor, DatabaseTemperatureSensorActor}
import akka.actor.Props
import commonsObjects.{Request, Wrapper, TemperatureSensorMonitoring}
import commonsObjects.List

import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._
import scala.util.parsing.json.JSONObject

/**
  * Servlet to handle request about the inside temperature.
  *
  * Can treat a GET request by sending the current temperature of the room.
  * Can treat a POST request by adjusting the room temperature according to a specific value.
  *
  * Created by Axel on 10-04-16.
  */
class InsideTemperatureServlet extends ScalaWebServerStack with JacksonJsonSupport {
  // Logger used for debugging
  private val logger = LoggerFactory.getLogger(getClass)


  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  /**
    * Handles a GET request by sending the current inside temperature in JSON
    */
  get("/") {
    logger.info("Get Request on /rest/insideTemperature")

    val myActor = actorSystem.actorOf(Props[DatabaseTemperatureSensorActor], "dbTempActor")

    val myTemperature = new TemperatureSensorMonitoring()
    val myWrapper = new Wrapper(Request.readTemperatureSensorMonitoringList, myTemperature)

    def futureResult = ask(myActor, myWrapper)
    val res = Await.result(futureResult, 30.seconds).asInstanceOf[List[TemperatureSensorMonitoring]]

    if(!res.isEmpty){
      val currentTemp = res.getLast
      val json = JSONObject.apply(Map(("value",currentTemp.getTemperature)))
      response.getWriter.write(json.toString())
    }

    myActor ! "Kill"
  }

  /**
    * Handles POST Request by adjusting the current temperature inside.
    */
  post("/"){
    logger.info("POST Request on /rest/insideTemperature")

    val json = readJsonFromBody(request.body)
    val value = json.children.head.extract[String]

//    val heatManagerActor = actorSystem.actorOf(Props[HeatManagerActor], "heatManagerActor")



    logger.info("The extracted value from the JSON is: " +value)

    // TODO: Send data to manager
    heatManagerActor ! "Increase Temperature " +value

    heatManagerActor ! "Kill"

    logger.info("POST Request done (Actor killed)")
  }
}

