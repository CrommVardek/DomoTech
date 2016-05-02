package server

import akka.actor.Props
import actors.{DatabaseSpiceActor, SpiceActor}
import commonsObjects._
import exception.SpiceNotPresentException

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


    var json = "["

    val iterator = res.iterator()

    while(iterator.hasNext){
      val spice = iterator.next()
      logger.info(spice.toString)
      json += JSONObject.apply(Map(("name",spice.getName),("description",spice.getDescription)))
      if(iterator.hasNext) json += ","
    }
    json += "]"

    // TODO: Parse JSON
    response.getWriter.write(json)
    actor ! "Kill"

    logger.info("Get Request done")
  }


  post("/"){
    logger.info("POST Request on /rest/spices")

    val json = readJsonFromBody(request.body)
    val value = json.children.head.extract[String]

    logger.info("The extracted value from the JSON is: " +value)

    try{
      logger.info("Asking the spice")
      val res = ManagersConfig.getInstance().getRoueEpices.askEpice(value);
      if (res) {response.setStatus(200);}
      else{ response.setStatus(666); response.getWriter.write("L'épice n'est pas sur la roue.")}
    } catch{case e:SpiceNotPresentException=> logger.info("Not present"); response.setStatus(501)}

    logger.info("POST Request done (Actor killed)")
  }
}
