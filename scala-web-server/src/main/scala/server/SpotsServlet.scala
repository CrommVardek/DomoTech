package server

import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory


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

    logger.info("GET Request successful")
  }


  post("/"){
    logger.info("POST Request on /rest/insideLuminosity")

    val json = readJsonFromBody(request.body)
    val value = json.children.head.extract[String]

    logger.info("The extracted value from the JSON is: " +value)

    // TODO: Send data to manager

    logger.info("POST Request done (Actor killed)")
  }
}
