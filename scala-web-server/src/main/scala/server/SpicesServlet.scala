package server

import akka.actor.Props
import actors.{DatabaseSpiceActor, DatabaseLightSensorActor, SpiceActor}
import commonsObjects.{Request, Wrapper, Spice, List}


import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.parsing.json.JSONObject



/**
  * Servlet to handle requests regarding the spices and the spice wheel.
  *
  * GET Request: Return the list of all spices currently on the spice wheel.
  * POST Request: Present the chosen spice on the wheel to the user.
  *
  * Created by Axel on 10-04-16.
  */
class SpicesServlet extends ScalaWebServerStack with JacksonJsonSupport {

  // Logger used for debugging
  private val logger = LoggerFactory.getLogger(getClass)
  // Actor to do the job
  private def actor = actorSystem.actorOf(Props[SpiceActor])


  before(){
    contentType=formats("json")
  }

  get("/") {
    logger.info("Get Request on /rest/spices")

    val actor = actorSystem.actorOf(Props[DatabaseSpiceActor], "dbSpiceActor")

    val spiceMonitoring = new Spice()
    val wrapper = new Wrapper(Request.readSpiceList, spiceMonitoring)

    def futureResult = ask(actor, wrapper)
    val res = Await.result(futureResult, 30.seconds).asInstanceOf[List[Spice]]

    logger.info("Result received!")
    logger.info(res.size().toString)


    var json = //"\"Spices\"" + ":["
                "["
    val iterator = res.iterator()
    logger.info("Iterator ready")
    while(iterator.hasNext){
      logger.info("In Iterator")
      val spice = iterator.next()
      logger.info(spice.toString)
      json += JSONObject.apply(Map(("name",spice.getName),("description",spice.getDescription)))
      if(iterator.hasNext) json += ","
    }
    json += "]"

    logger.info("Done iterating")

    // TODO: Parse JSON



      response.getWriter.write(json.toString())
      actor ! "Kill"

      logger.info("Get Request done")

/*


//      for (i <- (res.size()-1)){
      for(i:Spice <- test){
        json += JSONObject.apply(Map(("name",i.getName),("description",i.getDescription)))
        json += ","
      }
      json += "]"*/



  /*  if (!res.isEmpty){
      val currentLight = res.getLast
      val json = JSONObject.apply(Map(("value", currentLight.getInsideLight)))

      //      val json = JSONObject.apply(Map(("value",currentTemp.getTemperature)))

      response.getWriter.write(json.toString())
    } else {logger.info("is empty")}

    actor ! "Kill"

    logger.info("Get Request done (Actor killed)")
*/
  }


  post("/"){
    logger.info("POST Request on /rest/spices")

    val json = parsedBody.extract[JSONObject]
    val spice = json.obj.get("name")

    def futureResult = ask(actor, "Get spice")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Boolean]

    if (res){
      logger.info("POST Request successful")
      response.setStatus(200)
    } else{
      logger.info("POST Request failure")
      response.setStatus(500)
    }

    actor ! "Kill"
    logger.info("POST Request done (actor killed)")
  }
}
