package be.unamur.actors

import akka.actor.Actor
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

  def receive = {
    case "Get inside luminosity" => sender ! ""//LuminosityData.all
    case "Set inside luminosity" => sender ! true
    case "Kill" => context stop self
    case message:String => logger.info("Error: Message not understood...\t"+message)
  }
}



class LEDActor extends Actor{

  def receive = ???
}

/**
  * Actor to handle the Phidget Precision Light Sensor
  *
  * This Actor read the inside temperature of a room and sends it to the LightActor.
  */
class LightSensorActor(IFK:InterfaceKitPhidget, room:Integer) extends Actor{

  def receive = {
    case "Init" => ??? // Initialiaze the Sensor
    case "Stop" => context stop self
    case _ => ???
  }
}