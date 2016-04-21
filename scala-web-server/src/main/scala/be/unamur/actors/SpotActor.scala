package be.unamur.actors

import akka.actor.Actor
import be.unamur.models.SpotData
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 18-04-16.
  */
class SpotActor extends Actor{

  val logger =  LoggerFactory.getLogger(getClass)

  def receive = {
    case "Get number spots" => sender ! SpotData.all
    case "Kill" => context stop self
    case message:String => logger.info("Error: Message not understood...\t"+message)
  }
}
