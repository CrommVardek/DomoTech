package server

import akka.actor.Props
import actors.SpotActor
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory
import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._


import scala.util.parsing.json.JSONObject

/**
  * Created by Axel on 13-04-16.
  */
class SpotsServlet extends ScalaWebServerStack with JacksonJsonSupport {

  // Logger used for debugging
  private val logger = LoggerFactory.getLogger(getClass)



  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  get("/") {
    logger.info("Get Request on /rest/spices")

    response.getWriter.write("{\"value\" : \"6\"}")

    /*
    def futureResult = ask(actor, "Get number spots")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Spot]

    response.getWriter.write(res.toJson.toString())
*/

  }
}
