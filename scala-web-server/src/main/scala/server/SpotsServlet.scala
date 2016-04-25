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
  // Actor to do the job
  private def actor = actorSystem.actorOf(Props[SpotActor])


  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  get("/") {
    logger.info("Get Request on /rest/spices")
/*
    def futureResult = ask(actor, "Get number spots")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Spot]

    response.getWriter.write(res.toJson.toString())
*/
    actor ! "Kill"
  }



  post("/"){
    logger.info("POST Request on /rest/spots")

    val json = parsedBody.extract[JSONObject]
    val spot = json.obj.get("value")

    def futureResult = ask(actor, "Get desired spot")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Boolean]

    if (res){
      logger.info("POST Request successful.")
      response.setStatus(200)
    } else{
      logger.info("POST Request failure")
      response.setStatus(500)
    }

    actor ! "Kill"
    logger.info("POST Request done (actor killed)")
  }
}
