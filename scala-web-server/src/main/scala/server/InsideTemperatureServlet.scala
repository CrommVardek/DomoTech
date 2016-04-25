package server

import actors.HeatManagerActor
import akka.actor.Props


import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

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
  // Actor to do the job
  private def actor = actorSystem.actorOf(Props[HeatManagerActor])

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  /**
    * Handles a GET request by sending the current inside temperature in JSON
    */
  get("/") {
    logger.info("Get Request on /rest/insideTemperature")

    // Get the current inside temperature and wait for the result.
/*    def futureResult = ask(actor, "Get inside temperature")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[List[Temperature]]

    for (i <- res) response.getWriter.write(i.toJson.toString())
*/
    actor ! "Kill"
  }

  /**
    * Handles POST Request by adjusting the current temperature inside.
    */
  post("/"){
    logger.info("POST Request on /rest/insideTemperature")
    // Retrieve the JSON as a Temperature entity.
  /*  val temperature = parsedBody.extract[Temperature]

    def futureResult = ask(actor, "Set inside temperature")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Boolean]

    if (res){
      logger.info("POST Request successful.")
      response.setStatus(200)
    } else{
      logger.info("POST Request failure.")
      response.setStatus(500)
    }
*/
    actor ! "Kill"
    logger.info("POST Request done (Actor killed)")
  }
}

