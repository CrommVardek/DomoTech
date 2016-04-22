package be.unamur.actors

import akka.actor.Actor
//import be.unamur.commonsObjects.TemperatureData
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 10-04-16.
  */
class HeatManagerActor extends Actor{

  val logger =  LoggerFactory.getLogger(getClass)

  def receive = {
    case "Get inside temperature" => sender ! ""//TemperatureData.all
    case "Kill" => context stop self
    case message:String => logger.info("Error: Message not understood... \t"+message)
  }
}


class HeatSensorActor extends Actor{
  def receive = ???
}