package server

import actors.DatabaseLightSensorActor
import akka.actor.Props
import commonsObjects.{Wrapper, Request, LightSensorMonitoring, List}

import org.scalatra.json.JacksonJsonSupport
import akka.pattern.ask
import scala.concurrent.duration._
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.util.parsing.json.JSONObject

/**
  * Servlet to handle Requests regarding the inside luminosity.
  *
  * GET Request: Return the current luminosity degree in JSON.
  * POST Request: Adjust the current luminosity degree based on the input JSON.
  *
  * Created by Axel on 10-04-16.
  */
class InsideLuminosityServlet extends ScalaWebServerStack with JacksonJsonSupport {

  val logger = LoggerFactory.getLogger(getClass)


  before(){
    contentType= formats("json")
  }

  get("/") {
    logger.info("Get Request on /rest/insideLuminosity")

    val actor = actorSystem.actorOf(Props[DatabaseLightSensorActor], "dbLuminosityActor")

    val myLight = new LightSensorMonitoring()
    val myWrapper = new Wrapper(Request.readLightSensorMonitoringList, myLight)

    def futureResult = ask(actor, myWrapper)
    val res = Await.result(futureResult, 30.seconds).asInstanceOf[List[LightSensorMonitoring]]

    if (!res.isEmpty){
      val currentLight = res.getLast
      val json = JSONObject.apply(Map(("value", currentLight.getInsideLight)))
      response.getWriter.write(json.toString())
    } else {logger.info("is empty")}

    actor ! "Kill"

    logger.info("Get Request done (Actor killed)")
  }


  // TODO
  post("/"){
    logger.info("POST Request on /rest/insideLuminosity")

    val json = readJsonFromBody(request.body)
    val value = json.children.head.extract[String]

    logger.info("The extracted value from the JSON is: " +value)

    // TODO: Send data to manager

    logger.info("POST Request done (Actor killed)")
  }
}



