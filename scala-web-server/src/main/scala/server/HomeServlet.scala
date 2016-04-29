package server

import _root_.akka.actor.Props
import actors.{SpiceActor, LightManagerActor, HeatManagerActor}
import commonsObjects._
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._

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
