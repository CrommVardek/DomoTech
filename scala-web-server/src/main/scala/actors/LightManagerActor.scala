package actors

import akka.actor.Actor
import commonsObjects.{ManagersConfig, LightManager}

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

  private val manager:LightManager = ManagersConfig.getInstance().getLightManager;


  def receive = {
    case "Kill" => context stop self
    case message:String => {
                              logger.info("Message received = " + message)
                              if (message.startsWith("Increase Luminosity"))
                              {
                                // Retrieve the integer at the end of the message
                                val intensity = message.split(" ").last
                                manager.setLedPower(false, intensity.toInt)
                                manager.turnOnLeds()
                                logger.info("Message treated - Light was adjusted.")
                              }
                              else{
                                logger.info("Error: Message not understood... \t"+message)
                              }
                          }
  }
}
