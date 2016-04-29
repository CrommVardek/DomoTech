package actors

import akka.actor.Actor
import commonsObjects.LightManager

//import be.unamur.commonsObjects.LuminosityData
import com.phidgets.InterfaceKitPhidget
import org.slf4j.LoggerFactory

/**
  * Light Actor to handle all the managing of the lights.
  *
  * Created by Axel on 10-04-16.
  */
class LightManagerActor extends Actor{

  val logger =  LoggerFactory.getLogger(getClass)

  private var manager:LightManager = null

  def receive = {
    case manager:LightManager => this.manager = manager
    case "Kill" => context stop self
    case message:String => logger.info("Error: Message not understood...\t"+message)
  }
}
