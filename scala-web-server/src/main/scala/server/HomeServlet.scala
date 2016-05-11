package server

import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

class HomeServlet extends ScalaWebServerStack with JacksonJsonSupport {

  private val logger = LoggerFactory.getLogger(getClass)

  get("/") {
    logger.info("Get Request on main page")
    <html>
      <body>
        <h1>Welcome</h1>
        Welcome to the Home Automation Web Page
      </body>
    </html>
  }

  post("/"){
    logger.info("Post request on /")
    val json = readJsonFromBody(request.body)
    val value = json.children.head.extract[String]
    logger.info(value)
    response.getWriter.write(value)
  }
}
