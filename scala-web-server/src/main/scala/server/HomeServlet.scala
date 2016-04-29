package server

import _root_.akka.actor.Props
import actors.{SpiceActor, LightManagerActor, HeatManagerActor}
import commonsObjects._
import org.slf4j.LoggerFactory


class HomeServlet extends ScalaWebServerStack {

  private val logger = LoggerFactory.getLogger(getClass)
  var done:Boolean = false


  get("/") {
    logger.info("Get Request on main page")
    if(!done) {

      // Instantiation of managers and InterfaceKit actors
      val heatManagerActor = actorSystem.actorOf(Props[HeatManagerActor], "HeatManagerActor")
      val lightManagerActor = actorSystem.actorOf(Props[LightManagerActor], "LightManagerActor")

      logger.info("Heat and Light Actors initiated !")

      // Temporary Actor
      val tempActor = actorSystem.actorOf(Props[SpiceActor], "temporary")

      val heatManager = new HeatManager(1, 0.0)
      val lightManager = new LightManager(1, 0)

      logger.info("Managers initiated !")

      val interfaceKit = new InterfaceKit(heatManager, lightManager)

      logger.info("InterfaceKit instantiated !")

      heatManagerActor.tell(heatManager, tempActor)
      lightManagerActor.tell(lightManager, tempActor)

      logger.info("Messages sent to Actors !")

      done = true
    }

    <html>
      <body>
        <h1>Welcome</h1>
        Welcome to the Home Automation Web Page
      </body>
    </html>
  }
}
