package be.unamur.server

import akka.actor.Props
import be.unamur.actors.LightManagerActor

import org.scalatra.json.JacksonJsonSupport
import akka.pattern.ask
import org.slf4j.LoggerFactory

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

  def actor = actorSystem.actorOf(Props[LightManagerActor])

  before(){
    contentType= formats("json")
  }

  get("/") {
    logger.info("Get Request on /rest/insideLuminosity")

    def futureResult = ask(actor,"Get inside luminosity")

/*    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Luminosity]

    response.getWriter.write(res.toJson.toString())
*/
    actor ! "Kill"

    logger.info("Get Request done (Actor killed)")
  }

  // TODO
  post("/"){
    logger.info("POST Request on /rest/insideLuminosity")

  /*  val luminosity = parsedBody.extract[Luminosity]

    // TODO add luminosity value when asking actor.

    def futureResult = ask(actor, "Set inside luminosity")

    val res = Await.result(futureResult,15.seconds).asInstanceOf[Boolean]

    if (res) {
      response.setStatus(200)
      logger.info("Success")
    } else{
      logger.info("An error occured during the actor treatment...")
      response.setStatus(500)
    }
*/
    actor ! "Kill"

    logger.info("POST Request done (Actor killed)")
  }
}



