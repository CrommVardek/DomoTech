package server

import akka.actor.Props
import actors.SpiceActor
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.parsing.json.{JSONArray, JSONObject}

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

/*    def futureResult = ask(actor, "Get spices list")
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[List[Spice]]

    var json =  {
                  """Spices""" + ":["
                  //(for (i <- res) (i.toJson.toString() + ",")) + "]"
                }

    for (i <- res) (json += i.toJson.toString() + ",")

    json += "]"

    println(json)

    //response.getWriter.write(json.toString())

    for (i <- res) response.getWriter.write(i.toJson.toString())
*/
    actor ! "Kill"
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
