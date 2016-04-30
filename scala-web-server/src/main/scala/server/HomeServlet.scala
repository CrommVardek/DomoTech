package server

import org.slf4j.LoggerFactory

class HomeServlet extends ScalaWebServerStack {

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
}
